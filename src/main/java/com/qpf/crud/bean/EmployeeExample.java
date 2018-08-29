package com.qpf.crud.bean;

import java.util.ArrayList;
import java.util.List;

public class EmployeeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public EmployeeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andEmplIdIsNull() {
            addCriterion("empl_id is null");
            return (Criteria) this;
        }

        public Criteria andEmplIdIsNotNull() {
            addCriterion("empl_id is not null");
            return (Criteria) this;
        }

        public Criteria andEmplIdEqualTo(Integer value) {
            addCriterion("empl_id =", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdNotEqualTo(Integer value) {
            addCriterion("empl_id <>", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdGreaterThan(Integer value) {
            addCriterion("empl_id >", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("empl_id >=", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdLessThan(Integer value) {
            addCriterion("empl_id <", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdLessThanOrEqualTo(Integer value) {
            addCriterion("empl_id <=", value, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdIn(List<Integer> values) {
            addCriterion("empl_id in", values, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdNotIn(List<Integer> values) {
            addCriterion("empl_id not in", values, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdBetween(Integer value1, Integer value2) {
            addCriterion("empl_id between", value1, value2, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplIdNotBetween(Integer value1, Integer value2) {
            addCriterion("empl_id not between", value1, value2, "emplId");
            return (Criteria) this;
        }

        public Criteria andEmplNameIsNull() {
            addCriterion("empl_name is null");
            return (Criteria) this;
        }

        public Criteria andEmplNameIsNotNull() {
            addCriterion("empl_name is not null");
            return (Criteria) this;
        }

        public Criteria andEmplNameEqualTo(String value) {
            addCriterion("empl_name =", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameNotEqualTo(String value) {
            addCriterion("empl_name <>", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameGreaterThan(String value) {
            addCriterion("empl_name >", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameGreaterThanOrEqualTo(String value) {
            addCriterion("empl_name >=", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameLessThan(String value) {
            addCriterion("empl_name <", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameLessThanOrEqualTo(String value) {
            addCriterion("empl_name <=", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameLike(String value) {
            addCriterion("empl_name like", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameNotLike(String value) {
            addCriterion("empl_name not like", value, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameIn(List<String> values) {
            addCriterion("empl_name in", values, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameNotIn(List<String> values) {
            addCriterion("empl_name not in", values, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameBetween(String value1, String value2) {
            addCriterion("empl_name between", value1, value2, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplNameNotBetween(String value1, String value2) {
            addCriterion("empl_name not between", value1, value2, "emplName");
            return (Criteria) this;
        }

        public Criteria andEmplGenderIsNull() {
            addCriterion("empl_gender is null");
            return (Criteria) this;
        }

        public Criteria andEmplGenderIsNotNull() {
            addCriterion("empl_gender is not null");
            return (Criteria) this;
        }

        public Criteria andEmplGenderEqualTo(String value) {
            addCriterion("empl_gender =", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderNotEqualTo(String value) {
            addCriterion("empl_gender <>", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderGreaterThan(String value) {
            addCriterion("empl_gender >", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderGreaterThanOrEqualTo(String value) {
            addCriterion("empl_gender >=", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderLessThan(String value) {
            addCriterion("empl_gender <", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderLessThanOrEqualTo(String value) {
            addCriterion("empl_gender <=", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderLike(String value) {
            addCriterion("empl_gender like", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderNotLike(String value) {
            addCriterion("empl_gender not like", value, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderIn(List<String> values) {
            addCriterion("empl_gender in", values, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderNotIn(List<String> values) {
            addCriterion("empl_gender not in", values, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderBetween(String value1, String value2) {
            addCriterion("empl_gender between", value1, value2, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplGenderNotBetween(String value1, String value2) {
            addCriterion("empl_gender not between", value1, value2, "emplGender");
            return (Criteria) this;
        }

        public Criteria andEmplEmailIsNull() {
            addCriterion("empl_email is null");
            return (Criteria) this;
        }

        public Criteria andEmplEmailIsNotNull() {
            addCriterion("empl_email is not null");
            return (Criteria) this;
        }

        public Criteria andEmplEmailEqualTo(String value) {
            addCriterion("empl_email =", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailNotEqualTo(String value) {
            addCriterion("empl_email <>", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailGreaterThan(String value) {
            addCriterion("empl_email >", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailGreaterThanOrEqualTo(String value) {
            addCriterion("empl_email >=", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailLessThan(String value) {
            addCriterion("empl_email <", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailLessThanOrEqualTo(String value) {
            addCriterion("empl_email <=", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailLike(String value) {
            addCriterion("empl_email like", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailNotLike(String value) {
            addCriterion("empl_email not like", value, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailIn(List<String> values) {
            addCriterion("empl_email in", values, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailNotIn(List<String> values) {
            addCriterion("empl_email not in", values, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailBetween(String value1, String value2) {
            addCriterion("empl_email between", value1, value2, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andEmplEmailNotBetween(String value1, String value2) {
            addCriterion("empl_email not between", value1, value2, "emplEmail");
            return (Criteria) this;
        }

        public Criteria andDeptIdIsNull() {
            addCriterion("dept_id is null");
            return (Criteria) this;
        }

        public Criteria andDeptIdIsNotNull() {
            addCriterion("dept_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeptIdEqualTo(Integer value) {
            addCriterion("dept_id =", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdNotEqualTo(Integer value) {
            addCriterion("dept_id <>", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdGreaterThan(Integer value) {
            addCriterion("dept_id >", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("dept_id >=", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdLessThan(Integer value) {
            addCriterion("dept_id <", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdLessThanOrEqualTo(Integer value) {
            addCriterion("dept_id <=", value, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdIn(List<Integer> values) {
            addCriterion("dept_id in", values, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdNotIn(List<Integer> values) {
            addCriterion("dept_id not in", values, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdBetween(Integer value1, Integer value2) {
            addCriterion("dept_id between", value1, value2, "deptId");
            return (Criteria) this;
        }

        public Criteria andDeptIdNotBetween(Integer value1, Integer value2) {
            addCriterion("dept_id not between", value1, value2, "deptId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}