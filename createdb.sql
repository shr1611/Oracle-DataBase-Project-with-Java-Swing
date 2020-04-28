CREATE TABLE Business(
    business_id VARCHAR2(25) PRIMARY KEY,
    full_address VARCHAR2(300) NOT NULL,
    open VARCHAR2(5) NOT NULL,
--     main_category_name VARCHAR2(100) NOT NULL,
    city VARCHAR2(25)NOT NULL,
    review_count NUMBER,
    name  VARCHAR2(300) NOT NULL,
    longitude NUMBER NOT NULL,
    state VARCHAR2(3) NOT NULL,
    stars NUMBER,
    latitude NUMBER NOT NULL,
    type VARCHAR2(25) NOT NULL,
    attributes VARCHAR2(4000));

CREATE TABLE MainCategories(
--     business_id VARCHAR2(25),
    category_name VARCHAR2(100)
--     PRIMARY KEY(category_name, business_id),
--    FOREIGN KEY (business_id) REFERENCES Business(business_id) ON DELETE CASCADE
);

CREATE TABLE OnlyMainCategories(
    business_id VARCHAR2(25),
    main_category_name VARCHAR2(100),
    PRIMARY KEY(main_category_name, business_id),
    FOREIGN KEY (business_id) REFERENCES Business(business_id) ON DELETE CASCADE
);

CREATE TABLE OnlySubCategories(
    business_id VARCHAR2(25),
    sub_category_name VARCHAR2(100),
    PRIMARY KEY(business_id, sub_category_name),
    FOREIGN KEY (business_id) REFERENCES Business(business_id) ON DELETE CASCADE
);
CREATE TABLE SubCategories(
    business_id VARCHAR(25) NOT NULL,
     sub_category_name VARCHAR(100), --includes maincategories too
--      category_name VARCHAR(100) NOT NULL,
     PRIMARY KEY(business_id, sub_category_name),
     FOREIGN KEY (business_id) REFERENCES Business(business_id)ON DELETE CASCADE);

CREATE TABLE YelpUser(
   yelping_since DATE NOT NULL,
--    votes Vote,
   review_count INTEGER,
   user_name VARCHAR2(100) NOT NULL,
   user_id VARCHAR2(25) PRIMARY KEY,
--     friends 
    fans INTEGER,
    average_stars NUMBER,
    type VARCHAR(10) NOT NULL,
    compliment VARCHAR(1000),
    elite VARCHAR(100)
--     business_id VARCHAR(25),
--     FOREIGN KEY (business_id) REFERENCES Business(business_id) ON DELETE SET NULL
);

CREATE TABLE Reviews(
    user_id VARCHAR(25) NOT NULL,
    review_id VARCHAR(25) NOT NULL PRIMARY KEY,
    stars INTEGER,
    review_date DATE NOT NULL,
    text VARCHAR2(4000) NOT NULL,
    type VARCHAR2(20) NOT NULL,
    business_id VARCHAR(25) NOT NULL,
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
            ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES YelpUser(user_id)
            ON DELETE CASCADE,
    CHECK (stars>=1 AND stars <=5));

CREATE TABLE Attributes(
    business_id VARCHAR(25) NOT NULL,
    attributes VARCHAR(100) NOT NULL,
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
            ON DELETE CASCADE

);
CREATE TABLE Friends(
    friend_id VARCHAR(600), -- ??
    user_id VARCHAR(25),
    FOREIGN KEY (user_id) REFERENCES YelpUser(user_id)
            ON DELETE SET NULL);

CREATE TABLE Votes(
    review_id VARCHAR(25) PRIMARY KEY,
    funny VARCHAR2(10),
    useful VARCHAR2(10),
    cool VARCHAR2(10)
);
    




create index yelpuser_i on yelpUser ( yelping_since,user_name,user_id);
create index friend_i on friends (friend_id,user_id);
create index review_i on reviews(review_id,user_id,text,business_id);
create index business_i on business(business_id,city,state, stars);




