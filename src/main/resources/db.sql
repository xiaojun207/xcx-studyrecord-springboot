CREATE TABLE `Counters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` int(11) NOT NULL DEFAULT '1',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `WxAccount` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `unionid` varchar(64) DEFAULT '',
    `openid` varchar(64) NOT NULL DEFAULT '',
    `sessionKey` varchar(64) DEFAULT '',
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

