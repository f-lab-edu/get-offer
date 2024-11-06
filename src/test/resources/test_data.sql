TRUNCATE table USERS;
TRUNCATE table PRODUCTS;
TRUNCATE table AUCTION_RESULTS;


INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (1, 'test', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (2, 'test2', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
VALUES (3, 'test3', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
        '2024-01-01 00:00:00', '2024-01-01 00:00:00');


INSERT INTO PRODUCTS (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (1, 1, 'nintendo', '닌텐도 새 제품', 'GAMES', 10000, 5000, 'IN_PROGRESS', '2024-01-02 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
INSERT INTO PRODUCTS (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (2, 1, 'gucci belt', '어쩌구', 'CLOTHES', 230000, 50000, 'WAIT', '2024-01-03 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
INSERT INTO PRODUCTS (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
                      END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
VALUES (3, 1, 'ikea chair', '저쩌', 'FURNITURE', 43000, 8000, 'COMPLETED', '2024-01-06 00:00:00', '2024-01-04 00:00:00',
        '2024-01-02 00:00:00', '2024-01-02 00:00:00',
        '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
ALTER TABLE PRODUCTS ALTER COLUMN ID RESTART WITH 4;


INSERT INTO AUCTION_RESULTS (ID, PRODUCT_ID, BUYER_ID, FINAL_PRICE, AUCTION_STATUS, CREATED_AT, UPDATED_AT)
VALUES (1, 1, 2, 10000, 'COMPLETED', '2024-01-06 00:00:00', '2024-01-04 00:00:00');