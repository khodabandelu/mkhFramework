package org.mkh.frm.web.viewModel.security;


import org.mkh.frm.web.viewModel.BaseEntityViewModel;

public class ActionViewModel extends BaseEntityViewModel<Integer> {
	private String title = "";// onvane Menu
	private String src = "";// Manbae Manu
	private int width;
	private int height;
	private int sortOrder;
	private boolean enabled;
	private boolean forceReAuthenticate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isForceReAuthenticate() {
		return forceReAuthenticate;
	}

	public void setForceReAuthenticate(boolean forceReAuthenticate) {
		this.forceReAuthenticate = forceReAuthenticate;
	}
}
