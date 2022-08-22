package com.beansmith;

import lombok.Getter;

@Getter
public class ConstantParameter extends ProcessorParameter{
        private Object value;

        public ConstantParameter(String name, Object value) {
                super(name);
                this.value = value;
        }
}
