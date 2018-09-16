package org.mkh.frm.utility;

import org.hibernate.query.Query;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class HQLUtility {

    public static Map<String, Object> createJQL(StringBuffer jqlbuf, String filter, String order) {
        Map params = new HashMap<String, Object>();
        int start = jqlbuf.indexOf("order by");
        String oldOrder = null;
        if (start > -1) {
            oldOrder = jqlbuf.substring(start + 8).trim();
            jqlbuf = jqlbuf.replace(start, jqlbuf.length(), "");
        }

        if (filter != null && filter.length() > 0) {
            if (jqlbuf.indexOf(" where ") == -1) {
                jqlbuf.append(" where ");
                addFiter(jqlbuf, filter, params);
            } else {
                jqlbuf.append(" and ( ");
                addFiter(jqlbuf, filter, params);
                jqlbuf.append(")");
            }
        }
        if (order != null && order.length() > 0) {
            jqlbuf.append(" order by ").append(order);
            if (oldOrder != null) {
                jqlbuf.append(",").append(oldOrder);
            }
        } else if (oldOrder != null) {
            jqlbuf.append(" order by ").append(oldOrder);
        }
        return params;
    }

    public static StringBuffer retriveCountQueryFromHql(StringBuffer jql) {
        if (jql.indexOf("order by") >= 0)
            jql.replace(jql.indexOf("order by"), jql.length(), "");
        String mainQuery = jql.toString();

        jql = new StringBuffer(jql.toString().replace('\t', ' '));
        int firstIndexPBas = jql.indexOf(")");
        int firstIndexPBaz = jql.lastIndexOf("(", firstIndexPBas);
        while (firstIndexPBas > 0) {
            for (int i = firstIndexPBaz; i < firstIndexPBas + 1; i++)
                jql.replace(i, i + 1, "*");
            firstIndexPBas = jql.indexOf(")");
            firstIndexPBaz = jql.lastIndexOf("(", firstIndexPBas);
        }
        int Indexfrom = jql.indexOf(" from ");
        return new StringBuffer(" " + mainQuery.substring(Indexfrom, jql.length()));
    }

    @SuppressWarnings("unchecked")
    private static void addFiter(StringBuffer jqlbuf, String filterStr, Map params) {
        boolean addAnd = false;
        String[] pairs = filterStr.split("@;@");
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length() == 0)
                continue;
            String[] pair = pairs[i].split("@@");// Each pair is such this :
            // template@@fname@@value
            if (pair.length < 3 || pair[2] == null || pair[2].length() == 0) {
                jqlbuf.append(" 1=1 ");
                return;
            }
            String[] name = pair[1].split("@");
            if (name.length == 1) {// is simple field
                if (addAnd)
                    jqlbuf.append(" and ");
                addAnd = true;
                jqlbuf.append(pair[0].replaceAll("fname", name[0]).replaceAll("value", pair[2].trim()));
            } else {// is not simple field my be is a date field
                if (name[1].equals("from")) {// Date type. Has from and to date
                    if (addAnd)
                        jqlbuf.append(" and ");
                    addAnd = true;
                    params.put("param" + i + "_from", DateUtility.solarToDate(pair[2].trim()));// TODO
                    // lang?

                    if (filterStr.indexOf(name[0] + "@to") != -1) {
                        params.put("param" + i + "_to", DateUtility.solarToDate(pairs[i + 1].split("@@")[2].trim()));
                        jqlbuf.append(name[0]).append(" between :param").append(i).append("_from and :param").append(i).append("_to");
                    } else {
                        jqlbuf.append(name[0]).append(" >= :param").append(i).append("_from ");
                    }
                } else {
                    if (name[1].equals("to")) {
                        if (filterStr.indexOf(name[0] + "@from") != -1) {// Already
                            // added
                            // in
                            // from
                            // clause
                            // processing
                            continue;
                        } else {
                            if (addAnd)
                                jqlbuf.append(" and ");
                            addAnd = true;
                            params.put("param" + i + "_to", DateUtility.solarToDate(pair[2].trim()));// TODO
                            // lang?
                            jqlbuf.append(name[0]).append(" <= :param").append(i).append("_to ");
                        }
                    }
                }
            }
        }
    }

    public static String genSimpleFilterStr(String fieldName, String value, String... template) {
        String tmp = "fname='value'";
        if (template != null && template.length > 0) {
            tmp = template[0];
        }
        fieldName = fieldName == null ? "x" : fieldName;
        value = value == null ? "x" : value;
        return new StringBuffer(tmp).append("@@").append(fieldName).append("@@").append(value).append("@;@").toString();
    }

    public static String getDateFilterStr(String fieldName, String from, String to) {
        return genSimpleFilterStr(fieldName + "@from", from) + genSimpleFilterStr(fieldName + "@to", to);
    }

    public static String genFilterStr(Map<String, String> constraints) {
        if (constraints == null)
            return "";
        StringBuffer filter = new StringBuffer();
        for (Iterator iterator = constraints.entrySet().iterator(); iterator.hasNext(); ) {
            Entry<String, String> entry = (Entry<String, String>) iterator.next();
            filter.append(genSimpleFilterStr(entry.getKey(), entry.getValue()));
        }
        return filter.toString();
    }

    private static String searchPatern = "$$,$$%s$$%s$$%s$$,$$";
    private static String isEqual = "(%s = '%s')";// UI.General.Search.isEqual
    private static String isNotEqual = "(%s != '%s')";// UI.General.Search.isNotEqual
    private static String Larger = "(%s > '%s')";// UI.General.Search.Lager
    private static String lower = "(%s < '%s')"; // UI.General.Search.Lower
    private static String greaterThanOrEqual = "(%s >= '%s')"; // UI.General.Search.GreaterThanOrEqual
    private static String lessThanOrEqual = "(%s <= '%s')";// UI.General.Search.LessThanOrEqual
    private static String includeThisValue = "(%s Like '%s%s%s')";// UI.General.Search.IncludeThisValue
    private static String beginValue = "(%s Like '%s%s')"; // UI.General.Search.BeginValue
    private static String endValue = "(%s Like '%s%s')";// UI.General.Search.EndValue
    private static String notLike = "(%s Not Like '%s%s%s')"; // UI.General.Search.NotLike
    private static String notLikeBeginValue = "(%s Not Like '%s%s')";// UI.General.Search.NotLikeBeginValue
    private static String notLikeEndValue = "(%s Not Like '%s%s')";// UI.General.Search.NotLikeEndValue
    private static String between = "(%s Between %s And %s)"; // UI.General.Search.Between
    private static String notBetween = "(%s Not Between %s And %s)";// UI.General.Search.NotBetween

    public static String toHql(String temp) {
        String sentence[] = temp.split(Pattern.quote("$$,$$"));
        String totalSentence = "";
        for (int i = 1; i < sentence.length; i++) {
            String nested_sentence[] = sentence[i].split(Pattern.quote("$$"));
            if (!sentence[i].contains("@@")) {
                int count = 1;
                if (nested_sentence.length > 2)
                    count = Integer.parseInt(nested_sentence[2]);
                switch (count) {
                    case 1:
                        totalSentence += String.format(isEqual, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 2:
                        totalSentence += String.format(isNotEqual, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 3:
                        totalSentence += String.format(Larger, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 4:
                        totalSentence += String.format(lower, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 5:
                        totalSentence += String.format(greaterThanOrEqual, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 6:
                        totalSentence += String.format(lessThanOrEqual, nested_sentence[0], nested_sentence[1]);
                        break;
                    case 7:
                        totalSentence += String.format(includeThisValue, nested_sentence[0], "%", nested_sentence[1], "%");
                        break;
                    case 8:
                        totalSentence += String.format(endValue, nested_sentence[0], nested_sentence[1], "%");
                        break;
                    case 9:
                        totalSentence += String.format(beginValue, nested_sentence[0], "%", nested_sentence[1]);
                        break;
                    case 10:
                        totalSentence += String.format(notLike, nested_sentence[0], "%", nested_sentence[1], "%");
                        break;
                    case 11:
                        totalSentence += String.format(notLikeBeginValue, nested_sentence[0], "%", nested_sentence[1]);
                        break;
                    case 12:
                        totalSentence += String.format(notLikeEndValue, nested_sentence[0], nested_sentence[1], "%");
                        break;
                    default:
                        break;
                }
                if (i != sentence.length - 1) {
                    if (nested_sentence.length > 3)
                        totalSentence += " " + nested_sentence[3] + " ";
                    else
                        totalSentence += " And ";
                }
            } else {
                int count = 13;
                if (nested_sentence[2].length() > 2)
                    count = Integer.parseInt(nested_sentence[2]);
                String twoValue[] = nested_sentence[1].split(Pattern.quote("@@"));
                switch (count) {
                    case 13:
                        totalSentence += String.format(between, nested_sentence[0], twoValue[0], twoValue[1]);
                        break;
                    case 14:
                        totalSentence += String.format(notBetween, nested_sentence[0], twoValue[0], twoValue[1]);
                        break;
                    default:
                        break;
                }
                if (i != sentence.length - 1) {
                    if (nested_sentence.length > 3)
                        totalSentence += " " + nested_sentence[3] + " ";
                    else
                        totalSentence += " And ";
                }
            }
        }
        return totalSentence;
    }

    public static void toHQL(StringBuffer jqlbuf, String filter, String order) {
        int start = jqlbuf.indexOf("order by");
        String oldOrder = null;
        if (start > -1) {
            oldOrder = jqlbuf.substring(start + 8).trim();
            jqlbuf = jqlbuf.replace(start, jqlbuf.length(), "");
        }

        if (filter != null && filter.length() > 0) {
            if (jqlbuf.indexOf(" where ") < 0)
                jqlbuf.append(" where 1=1 ");

            String where = toHql(filter);
            if (where.length() > 0) {
                jqlbuf.append(" and ( ");
                jqlbuf.append(where);
                jqlbuf.append(" )");
            }
        }
        if (order != null && order.trim().length() > 0) {
            jqlbuf.append(" order by ").append(order);
            if (oldOrder != null) {
                jqlbuf.append(",").append(oldOrder);
            }
        } else if (oldOrder != null) {
            jqlbuf.append(" order by ").append(oldOrder);
        }
    }

    public static void setQueryParameters(Query query, Map hm) {
        Set set = hm.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Entry entry = (Entry) itr.next();
            if (entry.getValue() instanceof Integer)
                query.setInteger(entry.getKey().toString(), (Integer) entry.getValue());
            else if (entry.getValue() instanceof Date)
                query.setDate(entry.getKey().toString(), (Date) entry.getValue());
            else if (entry.getValue() instanceof Long)
                query.setLong(entry.getKey().toString(), (Long) entry.getValue());
            else if (entry.getValue() instanceof Set)
                query.setParameterList(entry.getKey().toString(), (Collection) entry.getValue());
            else if (entry.getValue() instanceof ArrayList)
                query.setParameterList(entry.getKey().toString(), (Collection) entry.getValue());
            else if (entry.getValue() instanceof Enum[])
                query.setParameterList(entry.getKey().toString(), (Enum[]) entry.getValue());
            else
                query.setParameter(entry.getKey().toString(), entry.getValue());
        }
    }

    public static String addHql(String propertyName, String propertyValue, SearchPatternType whereCluse) {
        // $$,$$propertyName$$propertyValue$$whereCluse$$,$$
        String totalSentence = "";
        switch (whereCluse) {
            case isEqual:
                totalSentence += String.format(isEqual, propertyName, propertyValue);
                break;
            case isNotEqual:
                totalSentence += String.format(isNotEqual, propertyName, propertyValue);
                break;
            case Larger:
                totalSentence += String.format(Larger, propertyName, propertyValue);
                break;
            case lower:
                totalSentence += String.format(lower, propertyName, propertyValue);
                break;
            case greaterThanOrEqual:
                totalSentence += String.format(greaterThanOrEqual, propertyName, propertyValue);
                break;
            case lessThanOrEqual:
                totalSentence += String.format(lessThanOrEqual, propertyName, propertyValue);
                break;
            case includeThisValue:
                totalSentence += String.format(includeThisValue, propertyName, "%", propertyValue, "%");
                break;
            case beginValue:
                totalSentence += String.format(endValue, propertyName, propertyValue, "%");
                break;
            case endValue:
                totalSentence += String.format(beginValue, propertyName, "%", propertyValue);
                break;
            case notLike:
                totalSentence += String.format(notLike, propertyName, "%", propertyValue, "%");
                break;
            case notLikeBeginValue:
                totalSentence += String.format(notLikeBeginValue, propertyName, "%", propertyValue);
                break;
            case notLikeEndValue:
                totalSentence += String.format(notLikeEndValue, propertyName, propertyValue, "%");
                break;
            case between:
                String split13[] = propertyValue.split(",");
                totalSentence += String.format(between, propertyName, split13[0], split13[1]);
                break;
            case notBetween:
                String split14[] = propertyValue.split(",");
                totalSentence += String.format(notBetween, propertyName, split14[0], split14[1]);
                break;
            default:
                break;
        }
        return totalSentence;
    }

    public static String addSearchOption(String propertyName, String propertyValue, SearchPatternType whereCluse) {
        // $$,$$propertyName$$propertyValue$$whereCluse$$,$$
        // $$,$$e.loTermination.ammunitionLo.id$$"+ammunitionLoId+"$$1$$,$$
        String totalSentence = "";
        totalSentence += String.format(searchPatern, propertyName, propertyValue, whereCluse.ordinal());
        return totalSentence;
    }

    public enum SearchPatternType {
        noThing, isEqual, // "(%s = '%s')",// UI.General.Search.isEqual
        isNotEqual, // "(%s != '%s')",// UI.General.Search.isNotEqual
        Larger, // "(%s > '%s')",// UI.General.Search.Lager
        lower, // "(%s < '%s')"; // UI.General.Search.Lower
        greaterThanOrEqual, // "(%s >= '%s')"; //
        // UI.General.Search.GreaterThanOrEqual
        lessThanOrEqual, // "(%s <= '%s')";// UI.General.Search.LessThanOrEqual
        includeThisValue, // "(%s Like '%s%s%s')";//
        // UI.General.Search.IncludeThisValue
        beginValue, // "(%s Like '%s%s')"; // UI.General.Search.BeginValue
        endValue, // "(%s Like '%s%s')";// UI.General.Search.EndValue
        notLike, // "(%s Not Like '%s%s%s')"; // UI.General.Search.NotLike
        notLikeBeginValue, // "(%s Not Like '%s%s')";//
        // UI.General.Search.NotLikeBeginValue
        notLikeEndValue, // "(%s Not Like '%s%s')";//
        // UI.General.Search.NotLikeEndValue
        between, // "(%s Between %s And %s)"; // UI.General.Search.Between
        notBetween // "(%s Not Between %s And %s)";//
        // UI.General.Search.NotBetween
    }

    public static void main2(String[] args) {
        String test = "select e from user e ";
        System.out.println(retriveCountQueryFromHql(new StringBuffer(test)));
    }
}
