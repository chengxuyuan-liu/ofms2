package com.example.demo.entity;

public class Permission {
    private Integer psiId;

    private String role;

    private Integer pAddDept;

    private Integer pEditDept;

    private Integer pAddMember;

    private Integer pEditMember;

    private Integer pDelMember;

    private Integer memberId;

    public Integer getPsiId() {
        return psiId;
    }

    public void setPsiId(Integer psiId) {
        this.psiId = psiId;
    }

    public Integer getpAddDept() {
        return pAddDept;
    }

    public void setpAddDept(Integer pAddDept) {
        this.pAddDept = pAddDept;
    }

    public Integer getpEditDept() {
        return pEditDept;
    }

    public void setpEditDept(Integer pEditDept) {
        this.pEditDept = pEditDept;
    }

    public Integer getpAddMember() {
        return pAddMember;
    }

    public void setpAddMember(Integer pAddMember) {
        this.pAddMember = pAddMember;
    }

    public Integer getpEditMember() {
        return pEditMember;
    }

    public void setpEditMember(Integer pEditMember) {
        this.pEditMember = pEditMember;
    }

    public Integer getpDelMember() {
        return pDelMember;
    }

    public void setpDelMember(Integer pDelMember) {
        this.pDelMember = pDelMember;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "psiId=" + psiId +
                ", role='" + role + '\'' +
                ", pAddDept=" + pAddDept +
                ", pEditDept=" + pEditDept +
                ", pAddMember=" + pAddMember +
                ", pEditMember=" + pEditMember +
                ", pDelMember=" + pDelMember +
                ", memberId=" + memberId +
                '}';
    }
}