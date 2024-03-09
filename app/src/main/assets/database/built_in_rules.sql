pragma foreign_keys=OFF;

BEGIN TRANSACTION;

CREATE TABLE `base_rule` (
    `id`                INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `title`             TEXT NOT NULL,
    `author_id`         TEXT NOT NULL,
    `author_type`       TEXT NOT NULL,
    `mutation_type`     TEXT NOT NULL,
    `trigger_domain`    TEXT NOT NULL,
    `date_modified`     INTEGER NOT NULL,
    `local_only`        INTEGER NOT NULL,
    `is_enabled`        INTEGER NOT NULL
);
INSERT INTO base_rule VALUES(
    1,
    'Spotify - tracking parameters',
    'system',
    'BuiltIn',
    'URL_PARAMS_SPECIFIC',
    'spotify.com',
    0,
    1,
    1
);
INSERT INTO base_rule VALUES(
    2,
    'Instagram - tracking parameters',
    'system',
    'BuiltIn',
    'URL_PARAMS_SPECIFIC',
    'instagram.com',
    0,
    1,
    1
);
INSERT INTO base_rule VALUES(
    3,
    'X.com to Twitter.com',
    'system',
    'BuiltIn',
    'DOMAIN_NAME_AND_URL_PARAMS_ALL',
    'x.com',
    0,
    1,
    1
);

CREATE TABLE `all_url_params_rule` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `base_rule_id` INTEGER NOT NULL,
    FOREIGN KEY(`base_rule_id`) REFERENCES `base_rule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE `domain_name_and_all_url_params_rule` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `base_rule_id` INTEGER NOT NULL,
    `initial_domain_name` TEXT NOT NULL,
    `target_domain_name` TEXT NOT NULL,
    FOREIGN KEY(`base_rule_id`) REFERENCES `base_rule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);
INSERT INTO domain_name_and_all_url_params_rule VALUES(
    1,
    3,
    'x.com',
    'twitter.com'
);

CREATE TABLE `domain_name_and_specific_url_params_rule` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `base_rule_id` INTEGER NOT NULL,
    `initial_domain_name` TEXT NOT NULL,
    `target_domain_name` TEXT NOT NULL,
    `removable_params` TEXT NOT NULL,
    FOREIGN KEY(`base_rule_id`) REFERENCES `base_rule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE `domain_name_rule` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `base_rule_id` INTEGER NOT NULL,
    `initial_domain_name` TEXT NOT NULL,
    `target_domain_name` TEXT NOT NULL,
    FOREIGN KEY(`base_rule_id`) REFERENCES `base_rule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE `specific_url_params_rule` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `base_rule_id` INTEGER NOT NULL,
    `removable_params` TEXT NOT NULL,
    FOREIGN KEY(`base_rule_id`) REFERENCES `base_rule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);
INSERT INTO specific_url_params_rule VALUES(
    1,
    1,
    'si,nd,_branch_match_id,_branch_referrer'
);
INSERT INTO specific_url_params_rule VALUES(
    2,
    2,
    'igshid'
);

DELETE FROM sqlite_sequence;

CREATE INDEX `index_all_url_params_rule_base_rule_id` ON `all_url_params_rule` (`base_rule_id`);
CREATE INDEX `index_domain_name_and_all_url_params_rule_base_rule_id` ON `domain_name_and_all_url_params_rule` (`base_rule_id`);
CREATE INDEX `index_domain_name_and_specific_url_params_rule_base_rule_id` ON `domain_name_and_specific_url_params_rule` (`base_rule_id`);
CREATE INDEX `index_domain_name_rule_base_rule_id` ON `domain_name_rule` (`base_rule_id`);
CREATE INDEX `index_specific_url_params_rule_base_rule_id` ON `specific_url_params_rule` (`base_rule_id`);

COMMIT;