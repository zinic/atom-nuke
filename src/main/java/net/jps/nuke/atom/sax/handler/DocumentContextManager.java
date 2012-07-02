package net.jps.nuke.atom.sax.handler;

import java.util.Stack;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.xml.AtomElement;

/**
 *
 * @author zinic
 */
public class DocumentContextManager {

   private final Stack<HandlerContext<?>> contextStack;

   public DocumentContextManager() {
      contextStack = new Stack<HandlerContext<?>>();
   }

   public boolean hasContext() {
      return !contextStack.empty();
   }

   public HandlerContext<?> peek() {
      return contextStack.peek();
   }

   public HandlerContext<?> pop() {
      return contextStack.pop();
   }

   public <T> HandlerContext<T> peek(Class<T> castAs) {
      final HandlerContext<?> entry = peek();

      if (castAs.isAssignableFrom(entry.builder().getClass())) {
         return (HandlerContext<T>) entry;
      }

      throw new IllegalArgumentException("Class: " + castAs.getName() + " is not a superclass or superinterface of: " + entry.builder().getClass().getName());
   }

   public <T> HandlerContext<T> pop(Class<T> castAs) {
      final HandlerContext<?> entry = pop();

      if (castAs.isAssignableFrom(entry.builder().getClass())) {
         return (HandlerContext<T>) entry;
      }

      throw new IllegalArgumentException("Class: " + castAs.getName() + " is not a superclass or superinterface of: " + entry.builder().getClass().getName());
   }

   public void push(AtomElement element, Object builder) {
      push(new HandlerContext(element, builder));
   }

   public void push(HandlerContext context) {
      contextStack.push(context);
   }
}
