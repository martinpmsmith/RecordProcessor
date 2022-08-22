package com.beansmith;

import lombok.Getter;

@Getter
public class InputParameter extends ProcessorParameter{
        private String name;
        private String sourceColumnName;

        public InputParameter(String name, String sourceColumnName) {
                super(name);
                this.sourceColumnName = sourceColumnName;
        }
}
