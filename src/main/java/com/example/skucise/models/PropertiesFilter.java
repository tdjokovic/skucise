package com.example.skucise.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PropertiesFilter extends Property{

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int pageNumber;

    @Min(5)
    @Max(Integer.MAX_VALUE)
    private int propertiesPerPage;

    private boolean ascendingOrder;

    public PropertiesFilter(){
        this.propertiesPerPage = 6;
        //da se prikaze paginacijom 6 po 6 strana
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPropertiesPerPage() {
        return propertiesPerPage;
    }

    public void setPropertiesPerPage(int propertiesPerPage) {
        this.propertiesPerPage = propertiesPerPage;
    }

    public boolean isAscendingOrder() {
        return ascendingOrder;
    }

    public void setAscendingOrder(boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }
}
