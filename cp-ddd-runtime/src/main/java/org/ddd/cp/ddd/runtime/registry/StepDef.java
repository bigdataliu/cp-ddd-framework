package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.annotation.Step;
import org.ddd.cp.ddd.model.IDomainStep;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 领域步骤的内部定义, internal usage only.
 */
@ToString
public class StepDef implements IRegistryAware {

    @Getter
    private String activity;
    
    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private String[] groups;

    @Getter
    private IDomainStep stepBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        Step domainStep = CoreAopUtils.getAnnotation(bean, Step.class);
        this.name = domainStep.name();
        this.groups = domainStep.groups();

        if (!(bean instanceof IDomainStep)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainStep");
        }
        this.stepBean = (IDomainStep) bean;
        this.activity = this.stepBean.activityCode();
        this.code = this.stepBean.stepCode();
        if (this.activity == null || this.activity.trim().isEmpty()) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " activityCode cannot be empty");
        }
        if (this.code == null || this.code.trim().isEmpty()) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " stepCode cannot be empty");
        }

        InternalIndexer.indexDomainStep(this);
    }
}