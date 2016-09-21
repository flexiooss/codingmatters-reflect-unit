package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.utils.MemberDeleguate;

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
