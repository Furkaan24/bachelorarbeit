package de.gfai.mobile.data.servlet.param;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Objects;
import java.util.function.Function;

public interface ServletRequestParameter
{

  default String getType(HttpServletRequest httpServletRequest)
  {
    return getString(httpServletRequest, "type");
  }

  default long getId(HttpServletRequest httpServletRequest)
  {
    return getLong(httpServletRequest, "id");
  }

  default int getInt(HttpServletRequest httpServletRequest, String name)
  {
    return getT(httpServletRequest, name, Integer::parseInt);
  }

  default long getLong(HttpServletRequest httpServletRequest, String name)
  {
    return getT(httpServletRequest, name, Long::parseLong);
  }

  default double getDouble(HttpServletRequest httpServletRequest, String name)
  {
    return getT(httpServletRequest, name, Double::parseDouble);
  }

  default <T> T getT(HttpServletRequest httpServletRequest, String name, Function<String, T> parseT)
  {
    String parameterValue = getString(httpServletRequest, name);

    try
    {
      return parseT.apply(parameterValue);
    }
    catch (RuntimeException ex)
    {
      throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
    }
  }

  default String getString(HttpServletRequest httpServletRequest, String name)
  {
    String parameterValue = httpServletRequest.getParameter(name);

    if (Objects.isNull(parameterValue))
      throw new WebApplicationException(new NullPointerException(name), Response.Status.NO_CONTENT);

    return parameterValue;
  }

  default boolean isPortRuleText(HttpServletRequest httpServletRequest) {
        String portRuleText = getString(httpServletRequest, "PortRuletext");
        return Boolean.parseBoolean(portRuleText);
    }
}
