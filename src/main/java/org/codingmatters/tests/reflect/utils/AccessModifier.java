package org.codingmatters.tests.reflect.utils;

/**
 * Created by nelt on 9/21/16.
 */
public enum AccessModifier {
    PUBLIC {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.public_(self);
        }
    },
    PRIVATE {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.private_(self);
        }
    },
    PROTECTED {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.protected_(self);
        }
    },
    PACKAGE_PRIVATE {
        @Override
        public void apply(MemberDeleguate deleguate, Object self) {
            deleguate.packagePrivate(self);
        }
    };

    public abstract void apply(MemberDeleguate deleguate, Object self);
}
