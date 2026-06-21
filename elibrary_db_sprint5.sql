--
-- PostgreSQL database dump
--

\restrict 35gXciXB6KZ2dTr6sbobGg1549Sfi88sUaFucRo3yHRhbs075iISp7uM7VrNhUR

-- Dumped from database version 16.14
-- Dumped by pg_dump version 16.14

-- Started on 2026-06-22 00:04:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 239 (class 1259 OID 17116)
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.audit_logs (
    id bigint NOT NULL,
    user_id integer,
    action character varying(100) NOT NULL,
    details text,
    "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 238 (class 1259 OID 17115)
-- Name: audit_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.audit_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 238
-- Name: audit_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.audit_logs_id_seq OWNED BY public.audit_logs.id;


--
-- TOC entry 222 (class 1259 OID 16970)
-- Name: authors; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.authors (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    biography text
);


--
-- TOC entry 221 (class 1259 OID 16969)
-- Name: authors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.authors_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 221
-- Name: authors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.authors_id_seq OWNED BY public.authors.id;


--
-- TOC entry 236 (class 1259 OID 17097)
-- Name: blockchain_ledger; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.blockchain_ledger (
    block_index integer NOT NULL,
    "timestamp" bigint NOT NULL,
    proof_of_work integer NOT NULL,
    previous_hash character varying(64) NOT NULL,
    current_hash character varying(64) NOT NULL
);


--
-- TOC entry 237 (class 1259 OID 17103)
-- Name: blockchain_transactions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.blockchain_transactions (
    tx_id character varying(64) NOT NULL,
    block_index integer NOT NULL,
    reference_type character varying(50) NOT NULL,
    reference_id integer NOT NULL,
    data_payload text NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 16978)
-- Name: book_authors; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_authors (
    book_id integer NOT NULL,
    author_id integer NOT NULL
);


--
-- TOC entry 227 (class 1259 OID 17003)
-- Name: book_copies; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_copies (
    id integer NOT NULL,
    book_id integer NOT NULL,
    branch_id integer NOT NULL,
    barcode character varying(100) NOT NULL,
    status character varying(30) DEFAULT 'AVAILABLE'::character varying NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 17002)
-- Name: book_copies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.book_copies_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 226
-- Name: book_copies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.book_copies_id_seq OWNED BY public.book_copies.id;


--
-- TOC entry 220 (class 1259 OID 16952)
-- Name: books; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.books (
    id integer NOT NULL,
    category_id integer NOT NULL,
    title character varying(255) NOT NULL,
    isbn character varying(20) NOT NULL,
    publisher character varying(150),
    publish_year integer,
    ebook_url character varying(512),
    cover_image character varying(512),
    view_count integer DEFAULT 0 NOT NULL,
    file_hash character varying(64)
);


--
-- TOC entry 219 (class 1259 OID 16951)
-- Name: books_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.books_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 219
-- Name: books_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.books_id_seq OWNED BY public.books.id;


--
-- TOC entry 229 (class 1259 OID 17024)
-- Name: borrowings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.borrowings (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_copy_id integer NOT NULL,
    borrow_date date NOT NULL,
    due_date date NOT NULL,
    return_date date,
    status character varying(30) DEFAULT 'BORROWING'::character varying NOT NULL,
    tx_hash character varying(64),
    block_index integer
);


--
-- TOC entry 228 (class 1259 OID 17023)
-- Name: borrowings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.borrowings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 228
-- Name: borrowings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.borrowings_id_seq OWNED BY public.borrowings.id;


--
-- TOC entry 225 (class 1259 OID 16994)
-- Name: branches; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.branches (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    location character varying(255) NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 16993)
-- Name: branches_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.branches_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 224
-- Name: branches_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.branches_id_seq OWNED BY public.branches.id;


--
-- TOC entry 218 (class 1259 OID 16941)
-- Name: categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categories (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


--
-- TOC entry 217 (class 1259 OID 16940)
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 217
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- TOC entry 231 (class 1259 OID 17043)
-- Name: fines; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fines (
    id integer NOT NULL,
    borrowing_id integer NOT NULL,
    fine_amount numeric(10,2) NOT NULL,
    status character varying(30) DEFAULT 'UNPAID'::character varying NOT NULL,
    tx_hash character varying(64),
    reason character varying(255) NOT NULL,
    user_id integer NOT NULL
);


--
-- TOC entry 230 (class 1259 OID 17042)
-- Name: fines_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fines_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 230
-- Name: fines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fines_id_seq OWNED BY public.fines.id;


--
-- TOC entry 235 (class 1259 OID 17077)
-- Name: reading_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reading_history (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_id integer NOT NULL,
    last_read_page integer DEFAULT 1 NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 234 (class 1259 OID 17076)
-- Name: reading_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reading_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5091 (class 0 OID 0)
-- Dependencies: 234
-- Name: reading_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reading_history_id_seq OWNED BY public.reading_history.id;


--
-- TOC entry 243 (class 1259 OID 17150)
-- Name: recommendations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recommendations (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_id integer NOT NULL,
    score double precision NOT NULL,
    calculated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 242 (class 1259 OID 17149)
-- Name: recommendations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.recommendations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5092 (class 0 OID 0)
-- Dependencies: 242
-- Name: recommendations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.recommendations_id_seq OWNED BY public.recommendations.id;


--
-- TOC entry 233 (class 1259 OID 17056)
-- Name: reviews; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reviews (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_id integer NOT NULL,
    rating integer NOT NULL,
    comment text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT reviews_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


--
-- TOC entry 232 (class 1259 OID 17055)
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reviews_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5093 (class 0 OID 0)
-- Dependencies: 232
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reviews_id_seq OWNED BY public.reviews.id;


--
-- TOC entry 241 (class 1259 OID 17132)
-- Name: user_preferences; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_preferences (
    id integer NOT NULL,
    user_id integer NOT NULL,
    category_id integer NOT NULL,
    interaction_type character varying(50) NOT NULL,
    weight integer DEFAULT 1 NOT NULL
);


--
-- TOC entry 240 (class 1259 OID 17131)
-- Name: user_preferences_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_preferences_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5094 (class 0 OID 0)
-- Dependencies: 240
-- Name: user_preferences_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_preferences_id_seq OWNED BY public.user_preferences.id;


--
-- TOC entry 216 (class 1259 OID 16923)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(100) NOT NULL,
    full_name character varying(100) NOT NULL,
    role character varying(30) DEFAULT 'ROLE_USER'::character varying NOT NULL,
    status boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 215 (class 1259 OID 16922)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5095 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 4828 (class 2604 OID 17119)
-- Name: audit_logs id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.audit_logs ALTER COLUMN id SET DEFAULT nextval('public.audit_logs_id_seq'::regclass);


--
-- TOC entry 4815 (class 2604 OID 16973)
-- Name: authors id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authors ALTER COLUMN id SET DEFAULT nextval('public.authors_id_seq'::regclass);


--
-- TOC entry 4817 (class 2604 OID 17006)
-- Name: book_copies id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_copies ALTER COLUMN id SET DEFAULT nextval('public.book_copies_id_seq'::regclass);


--
-- TOC entry 4813 (class 2604 OID 16955)
-- Name: books id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books ALTER COLUMN id SET DEFAULT nextval('public.books_id_seq'::regclass);


--
-- TOC entry 4819 (class 2604 OID 17027)
-- Name: borrowings id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.borrowings ALTER COLUMN id SET DEFAULT nextval('public.borrowings_id_seq'::regclass);


--
-- TOC entry 4816 (class 2604 OID 16997)
-- Name: branches id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.branches ALTER COLUMN id SET DEFAULT nextval('public.branches_id_seq'::regclass);


--
-- TOC entry 4812 (class 2604 OID 16944)
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- TOC entry 4821 (class 2604 OID 17046)
-- Name: fines id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fines ALTER COLUMN id SET DEFAULT nextval('public.fines_id_seq'::regclass);


--
-- TOC entry 4825 (class 2604 OID 17080)
-- Name: reading_history id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reading_history ALTER COLUMN id SET DEFAULT nextval('public.reading_history_id_seq'::regclass);


--
-- TOC entry 4832 (class 2604 OID 17153)
-- Name: recommendations id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations ALTER COLUMN id SET DEFAULT nextval('public.recommendations_id_seq'::regclass);


--
-- TOC entry 4823 (class 2604 OID 17059)
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- TOC entry 4830 (class 2604 OID 17135)
-- Name: user_preferences id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_preferences ALTER COLUMN id SET DEFAULT nextval('public.user_preferences_id_seq'::regclass);


--
-- TOC entry 4807 (class 2604 OID 16926)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 5073 (class 0 OID 17116)
-- Dependencies: 239
-- Data for Name: audit_logs; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5056 (class 0 OID 16970)
-- Dependencies: 222
-- Data for Name: authors; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5070 (class 0 OID 17097)
-- Dependencies: 236
-- Data for Name: blockchain_ledger; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.blockchain_ledger VALUES (1, 1781942108663, 1, '0000000000000000000000000000000000000000000000000000000000000000', '8f9c5595db198563540b38fa5f96dcfa2e76f500d15841a3a37c9412b12ad330');
INSERT INTO public.blockchain_ledger VALUES (2, 1781942184640, 1, '8f9c5595db198563540b38fa5f96dcfa2e76f500d15841a3a37c9412b12ad330', '1f15e2907bc8a67e57268b69cb3b8e09df8c0d48ac03ba3d3540ce82de7c2975');
INSERT INTO public.blockchain_ledger VALUES (3, 1781960605838, 1, '1f15e2907bc8a67e57268b69cb3b8e09df8c0d48ac03ba3d3540ce82de7c2975', '6b06733d909bfa7a12b44d448e4e7b05b0405dda73a864fa22f5dbcaaade7b8a');
INSERT INTO public.blockchain_ledger VALUES (4, 1781960742252, 1, '6b06733d909bfa7a12b44d448e4e7b05b0405dda73a864fa22f5dbcaaade7b8a', '1e558b4ec21b0e66beaf03287486155be59d0de744da1c73aa33c4228da8c259');
INSERT INTO public.blockchain_ledger VALUES (5, 1781961110403, 1, '1e558b4ec21b0e66beaf03287486155be59d0de744da1c73aa33c4228da8c259', 'ae7771027a685f60193b5ec9442a752fe7e5fa54e8d26c28b086a7b32052121a');
INSERT INTO public.blockchain_ledger VALUES (6, 1781961117747, 1, 'ae7771027a685f60193b5ec9442a752fe7e5fa54e8d26c28b086a7b32052121a', '18f1b3cd369e930329fddc7b4c614ed3af46ac4886ae50cab7c761841683f48e');
INSERT INTO public.blockchain_ledger VALUES (7, 1781961124706, 1, '18f1b3cd369e930329fddc7b4c614ed3af46ac4886ae50cab7c761841683f48e', '54a86b3553caa5f866e9f41a6e2a943097fad90e450047a4bb33683494de1c7f');
INSERT INTO public.blockchain_ledger VALUES (8, 1781961677222, 1, '54a86b3553caa5f866e9f41a6e2a943097fad90e450047a4bb33683494de1c7f', 'fe0fcf76519418bb480a41756db2cfa9190788b07d9a257634fc9606e023f8b7');
INSERT INTO public.blockchain_ledger VALUES (9, 1781961713156, 1, 'fe0fcf76519418bb480a41756db2cfa9190788b07d9a257634fc9606e023f8b7', '8e5cb3d7cc37a84868bcd97ea5008bd913f6b5c9e76efd61288833679bd5db1e');
INSERT INTO public.blockchain_ledger VALUES (10, 1781961720995, 1, '8e5cb3d7cc37a84868bcd97ea5008bd913f6b5c9e76efd61288833679bd5db1e', '4e6eb3eb2b8470746e04d13a64f839df4583a4882ee6eae62e6f0ee2eefbacf4');
INSERT INTO public.blockchain_ledger VALUES (11, 1782056234949, 1, '4e6eb3eb2b8470746e04d13a64f839df4583a4882ee6eae62e6f0ee2eefbacf4', '9a800d3adbbb3fa17565f6ce0eb6b34ded263333cc53cef42e64cc01083c62a0');
INSERT INTO public.blockchain_ledger VALUES (12, 1782057919538, 1, '9a800d3adbbb3fa17565f6ce0eb6b34ded263333cc53cef42e64cc01083c62a0', '11eb81764834af505ec187bca8d18703365f4d9f718d710f2b932c44dbe2246d');


--
-- TOC entry 5071 (class 0 OID 17103)
-- Dependencies: 237
-- Data for Name: blockchain_transactions; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.blockchain_transactions VALUES ('dee5175e4e3b48963e9d827b1d0a2dc7955c97af719c01d453ff6c12904c3442', 1, 'BORROWING', 2, '{"action":"RETURN", "userId":4, "bookCopyId":1, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('910d60a521233dc30a06af7d1c1bb708d69c0d2dc8dfddb07fb621638116f13d', 2, 'BORROWING', 3, '{"action":"RETURN", "userId":4, "bookCopyId":1, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('cf8bc53f01ec3d0499a2bcab428a4502a99c8dc3c8bb1e9b4dc240d653f920ae', 3, 'BORROWING', 4, '{"action":"BORROW", "userId":4, "bookCopyId":2, "borrowDate":"2026-06-20", "dueDate":"2026-06-27"}');
INSERT INTO public.blockchain_transactions VALUES ('30aaf105818b8271ce2e99e4deb02b20287840e52d129df7d5e6c6349ed090ab', 4, 'BORROWING', 4, '{"action":"RETURN", "userId":4, "bookCopyId":2, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('3ea0de45ba926bee5b0077e0dcc4d7c6ee329b3cb6cfea5e67028f344ed2464b', 5, 'BORROWING', 5, '{"action":"BORROW", "userId":4, "bookCopyId":1, "borrowDate":"2026-06-20", "dueDate":"2026-06-27"}');
INSERT INTO public.blockchain_transactions VALUES ('4eb92422e5d623bb7b6320137cf93a8d350fe4f009114bc0031b7670a4769bbe', 6, 'BORROWING', 6, '{"action":"BORROW", "userId":4, "bookCopyId":3, "borrowDate":"2026-06-20", "dueDate":"2026-06-27"}');
INSERT INTO public.blockchain_transactions VALUES ('24965d1a4722c70731bd9722bfa48e7d927168425825b97dc4ce739a59cb5df9', 7, 'BORROWING', 7, '{"action":"BORROW", "userId":4, "bookCopyId":2, "borrowDate":"2026-06-20", "dueDate":"2026-06-23"}');
INSERT INTO public.blockchain_transactions VALUES ('df19e90ccf7b7856ba56956919ba6f2f1534008303e19f724839699f255bedfe', 8, 'BORROWING', 5, '{"action":"RETURN", "userId":4, "bookCopyId":1, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('4c3359c9754f7bee9f9c59608a2dc24a91983cc19aec093f68d4045fe73ad9ec', 9, 'BORROWING', 6, '{"action":"RETURN", "userId":4, "bookCopyId":3, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('481ddaa48916aa95c4cce085a18623e6d3f10b2d5447dcaa9854ddf2dd7b7f4e', 10, 'BORROWING', 7, '{"action":"RETURN", "userId":4, "bookCopyId":2, "returnDate":"2026-06-20"}');
INSERT INTO public.blockchain_transactions VALUES ('250bd16477f6b418309079b93d16483a6ba59465a303dbb327093e8d770a36db', 11, 'BORROWING', 8, '{"action":"BORROW", "userId":5, "bookCopyId":3, "borrowDate":"2026-06-21", "dueDate":"2026-06-28"}');
INSERT INTO public.blockchain_transactions VALUES ('f23a65ac2f468dee9f8be70fd3ad6b50e4b6ec5b0f57edf468138438db0d2d92', 12, 'BORROWING', 9, '{"action":"BORROW", "userId":5, "bookCopyId":5, "borrowDate":"2026-06-21", "dueDate":"2026-06-28"}');


--
-- TOC entry 5057 (class 0 OID 16978)
-- Dependencies: 223
-- Data for Name: book_authors; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5061 (class 0 OID 17003)
-- Dependencies: 227
-- Data for Name: book_copies; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.book_copies VALUES (4, 1, 1, 'LIB-B1-FF1F4FEB', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (7, 1, 1, 'LIB-B1-EF89D0CC', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (8, 1, 1, 'LIB-B1-987526F0', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (9, 1, 1, 'LIB-B1-5DFC0074', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (10, 1, 1, 'LIB-B1-1E937614', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (6, 1, 1, 'LIB-B1-209E8A75', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (1, 1, 1, 'LIB-B1-7233D799', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (2, 1, 1, 'LIB-B1-8DD18058', 'AVAILABLE');
INSERT INTO public.book_copies VALUES (3, 1, 1, 'LIB-B1-101521F6', 'BORROWED');
INSERT INTO public.book_copies VALUES (5, 1, 1, 'LIB-B1-3C3F5AF6', 'BORROWED');


--
-- TOC entry 5054 (class 0 OID 16952)
-- Dependencies: 220
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.books VALUES (1, 1, 'Lập Trình Spring Boot Từ A-Z', 'ISBN-9999-8888', 'VKU Publisher', 2026, NULL, NULL, 0, NULL);


--
-- TOC entry 5063 (class 0 OID 17024)
-- Dependencies: 229
-- Data for Name: borrowings; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.borrowings VALUES (1, 4, 6, '2026-06-14', '2026-06-01', '2026-06-19', 'RETURNED', NULL, NULL);
INSERT INTO public.borrowings VALUES (2, 4, 1, '2026-06-20', '2026-07-04', '2026-06-20', 'RETURNED', 'dee5175e4e3b48963e9d827b1d0a2dc7955c97af719c01d453ff6c12904c3442', NULL);
INSERT INTO public.borrowings VALUES (3, 4, 1, '2026-06-20', '2026-07-04', '2026-06-20', 'RETURNED', '910d60a521233dc30a06af7d1c1bb708d69c0d2dc8dfddb07fb621638116f13d', NULL);
INSERT INTO public.borrowings VALUES (4, 4, 2, '2026-06-20', '2026-06-27', '2026-06-20', 'RETURNED', '30aaf105818b8271ce2e99e4deb02b20287840e52d129df7d5e6c6349ed090ab', NULL);
INSERT INTO public.borrowings VALUES (5, 4, 1, '2026-06-20', '2026-06-27', '2026-06-20', 'RETURNED', 'df19e90ccf7b7856ba56956919ba6f2f1534008303e19f724839699f255bedfe', NULL);
INSERT INTO public.borrowings VALUES (6, 4, 3, '2026-06-20', '2026-06-27', '2026-06-20', 'RETURNED', '4c3359c9754f7bee9f9c59608a2dc24a91983cc19aec093f68d4045fe73ad9ec', NULL);
INSERT INTO public.borrowings VALUES (7, 4, 2, '2026-06-20', '2026-06-23', '2026-06-20', 'RETURNED', '481ddaa48916aa95c4cce085a18623e6d3f10b2d5447dcaa9854ddf2dd7b7f4e', NULL);
INSERT INTO public.borrowings VALUES (8, 5, 3, '2026-06-21', '2026-06-28', NULL, 'BORROWING', '250bd16477f6b418309079b93d16483a6ba59465a303dbb327093e8d770a36db', NULL);
INSERT INTO public.borrowings VALUES (9, 5, 5, '2026-06-21', '2026-06-28', NULL, 'BORROWING', 'f23a65ac2f468dee9f8be70fd3ad6b50e4b6ec5b0f57edf468138438db0d2d92', NULL);


--
-- TOC entry 5059 (class 0 OID 16994)
-- Dependencies: 225
-- Data for Name: branches; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.branches VALUES (1, 'Thư viện Trung tâm - Khu A', 'Tầng 2, Tòa nhà Hiệu bộ Khu A');
INSERT INTO public.branches VALUES (2, 'Thư viện Số - Khu B', 'Tầng 1, Tòa nhà Đa năng Khu B');


--
-- TOC entry 5052 (class 0 OID 16941)
-- Dependencies: 218
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.categories VALUES (1, 'Lập Trình Java', 'Giáo trình về ngôn ngữ Java, cấu trúc Spring Boot và hệ sinh thái Backend liên quan.');
INSERT INTO public.categories VALUES (2, 'Khoa Học Máy Tính', 'Tài liệu lý thuyết về cấu trúc dữ liệu, giải thuật và hệ điều hành.');
INSERT INTO public.categories VALUES (3, 'Blockchain & Mật Mã Học', 'Tài liệu nghiên cứu hệ thống phi tập trung, mã hóa và bảo mật dữ liệu.');
INSERT INTO public.categories VALUES (4, 'Trí Tuệ Nhân Tạo & ML', 'Sách nghiên cứu về các mô hình toán học, học máy, học sâu.');


--
-- TOC entry 5065 (class 0 OID 17043)
-- Dependencies: 231
-- Data for Name: fines; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.fines VALUES (1, 1, 90000.00, 'UNPAID', NULL, 'Trả trễ 18 ngày', 4);
INSERT INTO public.fines VALUES (2, 1, 90000.00, 'UNPAID', NULL, 'Trả trễ 18 ngày', 4);
INSERT INTO public.fines VALUES (3, 1, 90000.00, 'UNPAID', NULL, 'Trả trễ 18 ngày', 4);


--
-- TOC entry 5069 (class 0 OID 17077)
-- Dependencies: 235
-- Data for Name: reading_history; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5077 (class 0 OID 17150)
-- Dependencies: 243
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5067 (class 0 OID 17056)
-- Dependencies: 233
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5075 (class 0 OID 17132)
-- Dependencies: 241
-- Data for Name: user_preferences; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5050 (class 0 OID 16923)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES (1, 'admin_sys', '$2a$10$dXJ3Z6bQ7G9bX8Yf1A2bC.e8V0uO1w2x3y4z5A6B7C8D9E0F1G2H3', 'admin@elibrary.edu.vn', 'Nguyễn Quản Trị Hệ Thống', 'ROLE_ADMIN', true, '2026-06-04 21:08:08.164871', '2026-06-04 21:08:08.164871');
INSERT INTO public.users VALUES (2, 'librarian_01', '$2a$10$dXJ3Z6bQ7G9bX8Yf1A2bC.e8V0uO1w2x3y4z5A6B7C8D9E0F1G2H3', 'thuthu01@elibrary.edu.vn', 'Trần Thị Thủ Thư', 'ROLE_LIBRARIAN', true, '2026-06-04 21:08:08.164871', '2026-06-04 21:08:08.164871');
INSERT INTO public.users VALUES (3, 'student_vku', '$2a$10$dXJ3Z6bQ7G9bX8Yf1A2bC.e8V0uO1w2x3y4z5A6B7C8D9E0F1G2H3', 'sinhvienvku@elibrary.edu.vn', 'Lê Văn Sinh Viên', 'ROLE_USER', true, '2026-06-04 21:08:08.164871', '2026-06-04 21:08:08.164871');
INSERT INTO public.users VALUES (4, 'tan_sinh_vien_01', '$2a$10$yxJa0fEvIqOSB7wrmFC2K.eGnnuteL5WgE6m5kwlGKm8Nn0btS7C6', 'tansinhvien@vku.udn.vn', 'Nguyễn Tân Sinh Viên', 'ROLE_USER', true, '2026-06-14 15:04:50.456631', '2026-06-14 15:04:50.456631');
INSERT INTO public.users VALUES (5, 'hacker_lord', '$2a$10$8MeC1tThPdeDPq6PSEXxfOV7IAjlfwReE7BwWeyTNVKNG3cHIwPA6', 'hacker@gmail.com', 'Mr Hacker', 'ROLE_USER', true, '2026-06-21 22:34:02.919833', '2026-06-21 22:34:02.919833');


--
-- TOC entry 5096 (class 0 OID 0)
-- Dependencies: 238
-- Name: audit_logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.audit_logs_id_seq', 1, false);


--
-- TOC entry 5097 (class 0 OID 0)
-- Dependencies: 221
-- Name: authors_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.authors_id_seq', 1, false);


--
-- TOC entry 5098 (class 0 OID 0)
-- Dependencies: 226
-- Name: book_copies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.book_copies_id_seq', 10, true);


--
-- TOC entry 5099 (class 0 OID 0)
-- Dependencies: 219
-- Name: books_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.books_id_seq', 1, true);


--
-- TOC entry 5100 (class 0 OID 0)
-- Dependencies: 228
-- Name: borrowings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.borrowings_id_seq', 9, true);


--
-- TOC entry 5101 (class 0 OID 0)
-- Dependencies: 224
-- Name: branches_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.branches_id_seq', 2, true);


--
-- TOC entry 5102 (class 0 OID 0)
-- Dependencies: 217
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.categories_id_seq', 4, true);


--
-- TOC entry 5103 (class 0 OID 0)
-- Dependencies: 230
-- Name: fines_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.fines_id_seq', 3, true);


--
-- TOC entry 5104 (class 0 OID 0)
-- Dependencies: 234
-- Name: reading_history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reading_history_id_seq', 1, false);


--
-- TOC entry 5105 (class 0 OID 0)
-- Dependencies: 242
-- Name: recommendations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.recommendations_id_seq', 1, false);


--
-- TOC entry 5106 (class 0 OID 0)
-- Dependencies: 232
-- Name: reviews_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reviews_id_seq', 1, false);


--
-- TOC entry 5107 (class 0 OID 0)
-- Dependencies: 240
-- Name: user_preferences_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_preferences_id_seq', 1, false);


--
-- TOC entry 5108 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_id_seq', 5, true);


--
-- TOC entry 4881 (class 2606 OID 17124)
-- Name: audit_logs audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_pkey PRIMARY KEY (id);


--
-- TOC entry 4852 (class 2606 OID 16977)
-- Name: authors authors_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_pkey PRIMARY KEY (id);


--
-- TOC entry 4876 (class 2606 OID 17101)
-- Name: blockchain_ledger blockchain_ledger_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.blockchain_ledger
    ADD CONSTRAINT blockchain_ledger_pkey PRIMARY KEY (block_index);


--
-- TOC entry 4879 (class 2606 OID 17109)
-- Name: blockchain_transactions blockchain_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.blockchain_transactions
    ADD CONSTRAINT blockchain_transactions_pkey PRIMARY KEY (tx_id);


--
-- TOC entry 4854 (class 2606 OID 16982)
-- Name: book_authors book_authors_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_authors
    ADD CONSTRAINT book_authors_pkey PRIMARY KEY (book_id, author_id);


--
-- TOC entry 4860 (class 2606 OID 17011)
-- Name: book_copies book_copies_barcode_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT book_copies_barcode_key UNIQUE (barcode);


--
-- TOC entry 4862 (class 2606 OID 17009)
-- Name: book_copies book_copies_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT book_copies_pkey PRIMARY KEY (id);


--
-- TOC entry 4847 (class 2606 OID 16962)
-- Name: books books_isbn_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_isbn_key UNIQUE (isbn);


--
-- TOC entry 4849 (class 2606 OID 16960)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- TOC entry 4865 (class 2606 OID 17030)
-- Name: borrowings borrowings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.borrowings
    ADD CONSTRAINT borrowings_pkey PRIMARY KEY (id);


--
-- TOC entry 4856 (class 2606 OID 17001)
-- Name: branches branches_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.branches
    ADD CONSTRAINT branches_name_key UNIQUE (name);


--
-- TOC entry 4858 (class 2606 OID 16999)
-- Name: branches branches_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.branches
    ADD CONSTRAINT branches_pkey PRIMARY KEY (id);


--
-- TOC entry 4843 (class 2606 OID 16950)
-- Name: categories categories_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


--
-- TOC entry 4845 (class 2606 OID 16948)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- TOC entry 4868 (class 2606 OID 17049)
-- Name: fines fines_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fines
    ADD CONSTRAINT fines_pkey PRIMARY KEY (id);


--
-- TOC entry 4872 (class 2606 OID 17084)
-- Name: reading_history reading_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reading_history
    ADD CONSTRAINT reading_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4886 (class 2606 OID 17156)
-- Name: recommendations recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (id);


--
-- TOC entry 4870 (class 2606 OID 17065)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 4874 (class 2606 OID 17086)
-- Name: reading_history uq_user_book_history; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reading_history
    ADD CONSTRAINT uq_user_book_history UNIQUE (user_id, book_id);


--
-- TOC entry 4883 (class 2606 OID 17138)
-- Name: user_preferences user_preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_preferences
    ADD CONSTRAINT user_preferences_pkey PRIMARY KEY (id);


--
-- TOC entry 4837 (class 2606 OID 16938)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4839 (class 2606 OID 16934)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4841 (class 2606 OID 16936)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4863 (class 1259 OID 17022)
-- Name: idx_barcode; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_barcode ON public.book_copies USING btree (barcode);


--
-- TOC entry 4877 (class 1259 OID 17102)
-- Name: idx_blocks; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_blocks ON public.blockchain_ledger USING btree (current_hash);


--
-- TOC entry 4850 (class 1259 OID 16968)
-- Name: idx_book_search; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_book_search ON public.books USING btree (title, isbn);


--
-- TOC entry 4866 (class 1259 OID 17041)
-- Name: idx_borrow_dates; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_borrow_dates ON public.borrowings USING btree (borrow_date, due_date);


--
-- TOC entry 4835 (class 1259 OID 16939)
-- Name: idx_user_auth; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_auth ON public.users USING btree (username, status);


--
-- TOC entry 4884 (class 1259 OID 17167)
-- Name: idx_user_rec; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_rec ON public.recommendations USING btree (user_id, score DESC);


--
-- TOC entry 4894 (class 2606 OID 24576)
-- Name: fines fk96m5dw0wvbckhnnwfeynue15y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fines
    ADD CONSTRAINT fk96m5dw0wvbckhnnwfeynue15y FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4888 (class 2606 OID 16988)
-- Name: book_authors fk_ba_authors; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_authors
    ADD CONSTRAINT fk_ba_authors FOREIGN KEY (author_id) REFERENCES public.authors(id) ON DELETE CASCADE;


--
-- TOC entry 4889 (class 2606 OID 16983)
-- Name: book_authors fk_ba_books; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_authors
    ADD CONSTRAINT fk_ba_books FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4887 (class 2606 OID 16963)
-- Name: books fk_books_categories; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT fk_books_categories FOREIGN KEY (category_id) REFERENCES public.categories(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4892 (class 2606 OID 17036)
-- Name: borrowings fk_borrowings_copies; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.borrowings
    ADD CONSTRAINT fk_borrowings_copies FOREIGN KEY (book_copy_id) REFERENCES public.book_copies(id) ON DELETE RESTRICT;


--
-- TOC entry 4893 (class 2606 OID 17031)
-- Name: borrowings fk_borrowings_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.borrowings
    ADD CONSTRAINT fk_borrowings_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- TOC entry 4900 (class 2606 OID 17110)
-- Name: blockchain_transactions fk_bt_ledger; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.blockchain_transactions
    ADD CONSTRAINT fk_bt_ledger FOREIGN KEY (block_index) REFERENCES public.blockchain_ledger(block_index) ON DELETE CASCADE;


--
-- TOC entry 4890 (class 2606 OID 17012)
-- Name: book_copies fk_copies_books; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT fk_copies_books FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4891 (class 2606 OID 17017)
-- Name: book_copies fk_copies_branches; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_copies
    ADD CONSTRAINT fk_copies_branches FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE RESTRICT;


--
-- TOC entry 4895 (class 2606 OID 17050)
-- Name: fines fk_fines_borrowings; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fines
    ADD CONSTRAINT fk_fines_borrowings FOREIGN KEY (borrowing_id) REFERENCES public.borrowings(id) ON DELETE RESTRICT;


--
-- TOC entry 4901 (class 2606 OID 17125)
-- Name: audit_logs fk_logs_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT fk_logs_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- TOC entry 4904 (class 2606 OID 17162)
-- Name: recommendations fk_rec_books; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT fk_rec_books FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4905 (class 2606 OID 17157)
-- Name: recommendations fk_rec_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT fk_rec_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4896 (class 2606 OID 17071)
-- Name: reviews fk_reviews_books; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk_reviews_books FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4897 (class 2606 OID 17066)
-- Name: reviews fk_reviews_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk_reviews_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4898 (class 2606 OID 17092)
-- Name: reading_history fk_rh_books; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reading_history
    ADD CONSTRAINT fk_rh_books FOREIGN KEY (book_id) REFERENCES public.books(id) ON DELETE CASCADE;


--
-- TOC entry 4899 (class 2606 OID 17087)
-- Name: reading_history fk_rh_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reading_history
    ADD CONSTRAINT fk_rh_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4902 (class 2606 OID 17144)
-- Name: user_preferences fk_up_categories; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_preferences
    ADD CONSTRAINT fk_up_categories FOREIGN KEY (category_id) REFERENCES public.categories(id) ON DELETE CASCADE;


--
-- TOC entry 4903 (class 2606 OID 17139)
-- Name: user_preferences fk_up_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_preferences
    ADD CONSTRAINT fk_up_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


-- Completed on 2026-06-22 00:04:40

--
-- PostgreSQL database dump complete
--

\unrestrict 35gXciXB6KZ2dTr6sbobGg1549Sfi88sUaFucRo3yHRhbs075iISp7uM7VrNhUR

