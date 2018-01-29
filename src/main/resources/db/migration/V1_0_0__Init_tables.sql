CREATE TABLE public.account_balance (
  account_balance_id int8 NOT NULL,
  balance_fact numeric(24,8) NOT NULL DEFAULT 0,
  balance_plan numeric(24,8) NOT NULL DEFAULT 0,
  balance_date timestamptz NULL,
  "version" int4 NULL,
  CONSTRAINT account_balance_pkey_s PRIMARY KEY (account_balance_id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.account (
  account_id int8 NOT NULL,
  account varchar(20) NOT NULL,
  "name" varchar(128) NOT NULL,
  account_type varchar(32) NOT NULL,
  currency varchar(12) NOT NULL,
  blocked bool NULL,
  closed bool NULL,
  create_date timestamptz NOT NULL,
  close_date timestamptz NULL,
  "comment" varchar(256) NULL,
  user_info_id int8 NOT NULL,
  "version" int4 NULL,
  restriction varchar(16) NULL,
  CONSTRAINT account_pkey_s PRIMARY KEY (account_id)
)
WITH (
  OIDS=FALSE
);

CREATE INDEX fki_userinfo_account_s ON public.account (user_info_id int8_ops);


-- CREATE TABLE public.circle (
--   radius  INT8
-- );
-- INSERT INTO public.circle (radius) VALUES (1), (2), (4), (5);


--TODO:: add more test data
INSERT INTO public.account_balance
(account_balance_id, balance_fact, balance_plan, balance_date, "version")
VALUES(5464, 10000.00000000, 10000.00000000, '2014-10-31 15:56:09.744', 0);

--TODO:: add more test data
INSERT INTO public.account
(account_id, account, "name", account_type, currency, blocked, closed, create_date, close_date, "comment", user_info_id, "version", restriction)
VALUES(5464, 'U0762515', 'USD', 'INTERNAL', 'USD', false, false, '2014-10-31 15:56:09.744', NULL, 'Automatically created account', 762, 0, NULL);