/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.info.report;

import de.gfai.infocable.geom.app.model.text.free.InstanceFreeTextDbObject;
import de.gfai.infocable.geom.app.model.text.free.PortFreeTextDbObject;
import de.gfai.infocable.geom.app.model.text.instance.InstanceTextDbObject;
import de.gfai.infocable.geom.app.model.text.instance.PortInstanceTextDbObject;
import de.gfai.infocable.geom.app.model.text.rule.RuleTextDbObject;
import de.gfai.infocable.model.catalog.Rack;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.instance.port.PortInstance;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 *
 * @author lost
 */
public interface PredicateIfcaObjects
{
  static final Predicate<String> PREDICATE_RACK = className -> Objects.equals(Rack.class.getSimpleName(), className);

  static final Predicate<String> PREDICATE_PORT_INSTANCE = className -> Objects.equals(PortInstance.class.getSimpleName(), className);

  static final Predicate<String> PREDICATE_PPV = className -> Objects.equals(Ppv.class.getSimpleName(), className);

  static final String[] TEXT_DB_CLASS_NAMES = new String[] {InstanceTextDbObject.class.getSimpleName(),
                                                            PortInstanceTextDbObject.class.getSimpleName(),
                                                            InstanceFreeTextDbObject.class.getSimpleName(),
                                                            PortFreeTextDbObject.class.getSimpleName(),
                                                            RuleTextDbObject.class.getSimpleName()
                                                          };


  static final Predicate<String> PREDICATE_TEXT_DB_OBJECT = className -> {
    return Arrays.stream(TEXT_DB_CLASS_NAMES)
                 .anyMatch(textDbClassName -> Objects.equals(textDbClassName, className));
  };

  default boolean isRackClass(String className)
  {
    return PREDICATE_RACK.test(className);
  }

  default boolean isPortInstance(String className)
  {
    return PREDICATE_PORT_INSTANCE.test(className);
  }

  default boolean isPpvClass(String className)
  {
    return PREDICATE_PPV.test(className);
  }

  default boolean isTextDbObjectClass(String className)
  {
    return PREDICATE_TEXT_DB_OBJECT.test(className);
  }
}
