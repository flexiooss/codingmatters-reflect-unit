package org.codingmatters.tests.reflect.matchers.support;

/**
 * Created by nelt on 9/21/16.
 */
public enum LevelModifier {
    INSTANCE {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.instance(self);
        }
    },
    STATIC {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.static_(self);
        }
    };

    public abstract void apply(MemberDeleguate deleguate, Object self);
}
