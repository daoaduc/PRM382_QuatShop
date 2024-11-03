package com.example.prm392.model;

import java.util.Date;

public class Promo {
    private int promoID;
    private String promoCode;
    private boolean promoType;
    private long promoValue;
    private Date startDate;
    private Date endDate;;
    private int maxUsage;
    private int usedCount;
    private boolean status;

    public Promo() {
    }

    public Promo(int promoID) {
        this.promoID = promoID;
    }

    public Promo(int promoID, String promoCode, boolean promoType, long promoValue, Date startDate, Date endDate, int maxUsage, int usedCount, boolean status) {
        this.promoID = promoID;
        this.promoCode = promoCode;
        this.promoType = promoType;
        this.promoValue = promoValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxUsage = maxUsage;
        this.usedCount = usedCount;
        this.status = status;
    }

    public int getPromoID() {
        return promoID;
    }
    public void setPromoID(int promoID) {
        this.promoID = promoID;
    }
    public String getPromoCode() {
        return promoCode;
    }
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
    public boolean isPromoType() {
        return promoType;
    }
    public void setPromoType(boolean promoType) {
        this.promoType = promoType;
    }
    public long getPromoValue() {
        return promoValue;
    }
    public void setPromoValue(long promoValue) {
        this.promoValue = promoValue;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public int getMaxUsage() {
        return maxUsage;
    }
    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }
    public int getUsedCount() {
        return usedCount;
    }
    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Promo{" +
                "promoID=" + promoID +
                ", promoCode='" + promoCode + '\'' +
                ", promoType=" + promoType +
                ", promoValue=" + promoValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", maxUsage=" + maxUsage +
                ", usedCount=" + usedCount +
                ", status=" + status +
                '}';
    }
}
