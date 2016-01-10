package com.cmbb.app.entity.capital;

import com.cmbb.app.entity.base.BaseEntity;

/**
 * Created by admin on 2015/12/29.
 */
public class CertificationEntity extends BaseEntity {
    private RelatedEntity related;

    public RelatedEntity getRelated() {
        return related;
    }

    public void setRelated(RelatedEntity related) {
        this.related = related;
    }
}
