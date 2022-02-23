package com.aquare.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author : dengtao aquare@163.com
 * createAt: 2022/2/23
 */
@SuppressWarnings("serial")

public class UserDetail implements UserDetails {
    
	private String id;
	
    private String username;
    
    private String password;
    
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastPasswordResetDate;
    // name:用户姓名
    private String name;


    // 登录时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp lastLoginDate;
    /**
     * 手机号码是否绑定账户
     */
    private Boolean isPhoneActive;
    
    private Boolean isReg = false;

    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList();

        return authorities;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) { this.lastPasswordResetDate = lastPasswordResetDate; }

	public Boolean getIsPhoneActive() {
		return isPhoneActive;
	}

	public void setIsPhoneActive(Boolean isPhoneActive) {
		this.isPhoneActive = isPhoneActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsReg() {
		return isReg;
	}

	public void setIsReg(Boolean isReg) {
		this.isReg = isReg;
	}

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
