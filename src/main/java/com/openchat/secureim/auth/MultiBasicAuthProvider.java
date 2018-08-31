package com.openchat.secureim.auth;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiBasicAuthProvider<T1,T2> implements InjectableProvider<Auth, Parameter> {

  private final Logger logger = LoggerFactory.getLogger(MultiBasicAuthProvider.class);

  private final BasicAuthProvider<T1> provider1;
  private final BasicAuthProvider<T2> provider2;

  private final Class<?> clazz1;
  private final Class<?> clazz2;

  public MultiBasicAuthProvider(Authenticator<BasicCredentials, T1> authenticator1,
                                Class<?> clazz1,
                                Authenticator<BasicCredentials, T2> authenticator2,
                                Class<?> clazz2,
                                String realm)
  {
    this.provider1 = new BasicAuthProvider<T1>(authenticator1, realm);
    this.provider2 = new BasicAuthProvider<T2>(authenticator2, realm);
    this.clazz1    = clazz1;
    this.clazz2    = clazz2;
  }


  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

  @Override
  public Injectable<?> getInjectable(ComponentContext componentContext,
                                     Auth auth, Parameter parameter)
  {
    if (parameter.getParameterClass().equals(clazz1)) {
      return this.provider1.getInjectable(componentContext, auth, parameter);
    } else {
      return this.provider2.getInjectable(componentContext, auth, parameter);
    }
  }
}
