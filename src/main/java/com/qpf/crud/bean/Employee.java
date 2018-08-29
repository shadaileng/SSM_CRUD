package com.qpf.crud.bean;

public class Employee {
    private Integer emplId;

    private String emplName;

    private String emplGender;

    private String emplEmail;

    private Integer deptId;
    
    private Department department;
    
    public Employee() {}

    public Employee(Integer emplId, String emplName, String emplGender, String emplEmail, Integer deptId) {
		super();
		this.emplId = emplId;
		this.emplName = emplName;
		this.emplGender = emplGender;
		this.emplEmail = emplEmail;
		this.deptId = deptId;
	}

	public Integer getEmplId() {
        return emplId;
    }

    public void setEmplId(Integer emplId) {
        this.emplId = emplId;
    }

    public String getEmplName() {
        return emplName;
    }

    public void setEmplName(String emplName) {
        this.emplName = emplName == null ? null : emplName.trim();
    }

    public String getEmplGender() {
        return emplGender;
    }

    public void setEmplGender(String emplGender) {
        this.emplGender = emplGender == null ? null : emplGender.trim();
    }

    public String getEmplEmail() {
        return emplEmail;
    }

    public void setEmplEmail(String emplEmail) {
        this.emplEmail = emplEmail == null ? null : emplEmail.trim();
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [emplId=" + emplId + ", emplName=" + emplName + ", emplGender=" + emplGender + ", emplEmail="
				+ emplEmail + ", deptId=" + deptId + ", department=" + department + "]";
	}
    
}