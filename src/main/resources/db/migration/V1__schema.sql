CREATE TABLE product (
    id SERIAL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR (255) NOT NULL,
    price NUMERIC NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_product_id PRIMARY KEY (id)
);

CREATE TABLE account (
    id SERIAL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR (255) NOT NULL,
    wallet NUMERIC NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
);

CREATE TABLE role (
    id SERIAL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_id PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
);

CREATE TABLE account_role (
    id SERIAL,
    account_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT pk_account_role_id PRIMARY KEY (id),
    CONSTRAINT fk_account_role_account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_account_role_role_id FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE account_product (
    id SERIAL,
    account_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    CONSTRAINT pk_account_product_id PRIMARY KEY (id),
    CONSTRAINT fk_account_product_account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_account_product_product_id FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE ON UPDATE CASCADE
);
