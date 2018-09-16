
package org.mkh.frm.web.viewModel.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mkh.frm.web.viewModel.BaseEntityViewModel;

import java.util.Date;

public class LoginHistoryViewModel extends BaseEntityViewModel<Long> {
	private String username;
	private String password;
	private Date loginDate;
	private String loginSolarDate;
	private boolean success;//aya vorood moaffagit amiz bood
	private boolean active;//baraye sefr kardane shomarande
	private String loginBrowser;
	private String loginOperatingSystem;
	private String loginBrowserVersion;
	private String loginResolution;
	private boolean login;//aya in amal login boode ast ya log out.
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getLoginBrowser() {
		return loginBrowser;
	}
	public void setLoginBrowser(String loginBrowser) {
		this.loginBrowser = loginBrowser;
	}
	public String getLoginOperatingSystem() {
		return loginOperatingSystem;
	}
	public void setLoginOperatingSystem(String loginOperatingSystem) {
		this.loginOperatingSystem = loginOperatingSystem;
	}
	public String getLoginBrowserVersion() {
		return loginBrowserVersion;
	}
	public void setLoginBrowserVersion(String loginBrowserVersion) {
		this.loginBrowserVersion = loginBrowserVersion;
	}
	public String getLoginResolution() {
		return loginResolution;
	}
	public void setLoginResolution(String loginResolution) {
		this.loginResolution = loginResolution;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setLoginSolarDate(String loginSolarDate) {
		this.loginSolarDate = loginSolarDate;
	}
	public String getLoginSolarDate() {
		return loginSolarDate;
	}
	@JsonProperty("ip")
	public String getLogIp() {
		return super.getIp();
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Date getLoginDate() {
		return loginDate;
	}

}
