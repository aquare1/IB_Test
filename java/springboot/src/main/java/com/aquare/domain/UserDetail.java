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
 * createAt: 2019/5/14
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

    //regionId：区域机构编号，
    private Long regionId;
    // dataOrgId:归属分公司机构编号
    private Long dataOrgId;
    // 机构编号
    private Long orgId;
    // 部门ID
    private Long organizationId;
    // 部门名称
    private String orgName;

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
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "/post-person";
            }
        });

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "/delete-person/*";
            }
        });
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

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *  账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     */
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

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getDataOrgId() {
        return dataOrgId;
    }

    public void setDataOrgId(Long dataOrgId) {
        this.dataOrgId = dataOrgId;
    }


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
