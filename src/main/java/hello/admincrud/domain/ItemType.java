package hello.admincrud.domain;

public enum ItemType {
    HIGHEST("최상"), BEST("상"), LOWER("중");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
