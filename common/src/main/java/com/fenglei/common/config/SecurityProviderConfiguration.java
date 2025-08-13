package com.fenglei.common.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import java.security.Security;


@Configuration
public class SecurityProviderConfiguration
{
	static
	{
		Security.addProvider(new BouncyCastleProvider());
	}
}
