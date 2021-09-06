package greatlirik.WebStore.entities;

public enum  Role {
    USER("ROLE_USER"),
    MODER("ROLE_MODER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
