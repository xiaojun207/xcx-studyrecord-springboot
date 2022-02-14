CREATE TABLE `Counters`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `count`     int(11) NOT NULL DEFAULT '1',
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `WxAccount`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `nickName`   varchar(64)          DEFAULT '',
    `avatarUrl`  varchar(64)          DEFAULT '',
    `mobile`     varchar(64)          DEFAULT '',
    `gender`     int(11) DEFAULT '' comment '性别 0：未知、1：男、2：女',
    `country`    varchar(64)          DEFAULT '',
    `province`   varchar(64)          DEFAULT '',
    `city`       varchar(64)          DEFAULT '',
    `unionid`    varchar(64)          DEFAULT '',
    `openid`     varchar(64) NOT NULL DEFAULT '',
    `sessionKey` varchar(64)          DEFAULT '',
    `createdAt`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updatedAt`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Project`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `name`      varchar(20) NULL,
    `icon`      varchar(20) NULL,
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `UserTest`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `uid`         int(11) NOT NULL,
    `projectId`   int(11) NOT NULL,
    `projectName` varchar(20) NULL,
    `result`      double(255, 0
) DEFAULT '0',
  `score` double(255, 0) DEFAULT '0',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `Family`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `headUid`   int(11) NOT NULL comment '家庭之主Uid',
    `memberUid` int(11) NOT NULL comment '家庭成员Uid',
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
