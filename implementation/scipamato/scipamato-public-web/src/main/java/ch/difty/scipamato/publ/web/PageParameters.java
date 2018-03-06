package ch.difty.scipamato.publ.web;

public enum PageParameters {

    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    SHOW_NAVBAR("showNavbar");

    private final String name;

    PageParameters(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
