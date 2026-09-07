package com.zestmarket.order.dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private Double totalRevenue;
    private Long recentOrdersCount;
    private Long pendingOrdersCount;
    private Long confirmedOrdersCount;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(Double totalRevenue, Long recentOrdersCount, Long pendingOrdersCount, Long confirmedOrdersCount) {
        this.totalRevenue = totalRevenue;
        this.recentOrdersCount = recentOrdersCount;
        this.pendingOrdersCount = pendingOrdersCount;
        this.confirmedOrdersCount = confirmedOrdersCount;
    }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getRecentOrdersCount() { return recentOrdersCount; }
    public void setRecentOrdersCount(Long recentOrdersCount) { this.recentOrdersCount = recentOrdersCount; }

    public Long getPendingOrdersCount() { return pendingOrdersCount; }
    public void setPendingOrdersCount(Long pendingOrdersCount) { this.pendingOrdersCount = pendingOrdersCount; }

    public Long getConfirmedOrdersCount() { return confirmedOrdersCount; }
    public void setConfirmedOrdersCount(Long confirmedOrdersCount) { this.confirmedOrdersCount = confirmedOrdersCount; }
}
