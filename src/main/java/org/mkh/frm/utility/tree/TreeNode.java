package org.mkh.frm.utility.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {

	@JsonView(TreeNodeView.lazy.class)
	private String id;
	
	@JsonView(TreeNodeView.lazy.class)
	private String text;
	
	@JsonView(TreeNodeView.lazy.class)
	private TreeNodeState state = TreeNodeState.open;
	
	@JsonView(TreeNodeView.lazy.class)
	private Boolean checked=false;

	@JsonView(TreeNodeView.lazy.class)
	@JsonProperty("attributes")
	private Map<String, String> attrMap = new HashMap<String, String>();
	
	@JsonView(TreeNodeView.eager.class)
	@JsonProperty("children")
	private List<TreeNode> childs;

	@JsonIgnore
	private TreeNode parent;
	
	@JsonIgnore
	private Integer childCount;

	public TreeNode() {
	}

	public TreeNode(String id, String text) {
		this.id = id;
		this.text = text;
		this.childCount = 0;
	}
	public TreeNode(String id, String text, TreeNodeState state) {
		this(id, text);
		this.state = state;
	}
	public TreeNode(String id, String text, TreeNodeState state,Boolean checked) {
		this(id, text,state);
		this.checked = checked;
	}

	public TreeNode addAttr(String attrName, String attrValue) {
		attrMap.put(attrName, attrValue);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChilds() {
		if (childs == null) {
			childs = new ArrayList<TreeNode>();
		}
		return childs;
	}

	public void setChilds(List<TreeNode> childs) {
		this.childs = childs;
	}

	public Map<String, String> getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public TreeNodeState getState() {
		return state;
	}

	public void setState(TreeNodeState state) {
		this.state = state;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}