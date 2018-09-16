package org.mkh.frm.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.mkh.frm.domain.BaseEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "core_action")
@Getter
@Setter
public class Action extends BaseEntity<Integer> {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "src")
    private String src;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "icon_Name")
    private String iconName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Action parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Action> childs;

    @JsonIgnore
    @ManyToMany(mappedBy = "actions")
    private Set<Group> groups;


}
