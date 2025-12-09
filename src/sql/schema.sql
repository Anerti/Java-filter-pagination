CREATE TABLE product_management_app.product(
                                               id SERIAL PRIMARY KEY,
                                               name VARCHAR(20) NOT NULL,
                                               price DOUBLE PRECISION NOT NULL CHECK (price > 0),
                                               creation_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_management_app.product_category(
                                                        id SERIAL PRIMARY KEY,
                                                        name VARCHAR(20) NOT NULL,
                                                        product_id int NOT NULL,
                                                        CONSTRAINT fk_product_category
                                                            FOREIGN KEY (product_id)
                                                                REFERENCES product_management_app.product(id)
);