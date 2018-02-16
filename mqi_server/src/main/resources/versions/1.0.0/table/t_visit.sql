drop table if exists t_visit;
create table if not exists t_visit (
    visit_id bigserial primary key,
    patient_id bigint not null,
    admit_dt timestamp,
    covered_days integer,
    cpt varchar(255),
    cpt2 varchar(255),
    cpt_mod1 varchar(255),
    cpt_mod2 varchar(255),
    date_of_service timestamp,
    denied boolean,
    diag1 varchar(255),
    diag10 varchar(255),
    diag2 varchar(255),
    diag3 varchar(255),
    diag4 varchar(255),
    diag5 varchar(255),
    diag6 varchar(255),
    diag7 varchar(255),
    diag8 varchar(255),
    diag9 varchar(255),
    discharge_dt timestamp,
    discharge_status integer,
    drg varchar(255),
    drg_version integer,
    hcpcs varchar(255),
    icd_version integer,
    place_of_service varchar(255),
    proc1 varchar(255),
    proc10 varchar(255),
    proc2 varchar(255),
    proc3 varchar(255),
    proc4 varchar(255),
    proc5 varchar(255),
    proc6 varchar(255),
    proc7 varchar(255),
    proc8 varchar(255),
    proc9 varchar(255),
    provider_id varchar(255),
    supplemental boolean,
    type_of_bill varchar(255),
    ubrev varchar(255),
    units varchar(255)
);