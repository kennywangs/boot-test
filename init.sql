INSERT INTO `id_group` (`id`, `create_date`, `modify_date`, `name`, `group_no`, `type`) VALUES ('2B214496-70C8-47D5-8C7C-DBA4CD4254A1', '2018-08-24 15:50:30', '2018-08-24 15:50:31', 'xxb', 'xxb', 1);
INSERT INTO `id_user` (`id`, `create_date`, `description`, `mobile`, `modify_date`, `name`, `open_id`, `password`, `source`, `type`, `group_id`) VALUES ('BCB7CAD0-5C22-460E-88FD-19E3816F0F8D', '2018-08-24 15:46:44', '系统管理员', '13888699392', '2018-08-24 15:47:18', 'admin', NULL, 'e10adc3949ba59abbe56e057f20f883e', NULL, 1, '2B214496-70C8-47D5-8C7C-DBA4CD4254A1');
INSERT INTO `id_authority` (`id`, `auth_type`, `auth_url`, `create_date`, `description`, `modify_date`, `name`, `need_auth`) VALUES ('f2852721-58df-456e-99bc-7bba1d6a3e4c', 10, '/auth/**', '2018-06-20 15:53:39.708', '授权权限', '2018-06-20 15:53:39.708', 'authurl', 'true');
INSERT INTO `id_authority` (`id`, `auth_type`, `auth_url`, `create_date`, `description`, `modify_date`, `name`, `need_auth`) VALUES ('31c74782-b4fd-445c-ba28-34aa537a4d64', 10, '/user/**', '2018-06-21 16:51:36.426', '用户权限', '2018-06-21 16:51:36.426', 'userurl', 'true');
INSERT INTO `id_role` (`id`, `create_date`, `description`, `modify_date`, `name`, `role_type`) VALUES ('800c9956-b35b-471d-92d7-ee1911f5454c', '2018-06-20 15:38:40.569', '系统管理员', '2018-06-20 15:38:40.569', 'admin', 10);
