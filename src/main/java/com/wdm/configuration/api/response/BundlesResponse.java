package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Bundle;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BundlesResponse {
    private Set<Bundle> bundle;

    public static BundlesResponseBuilder builder() {
        return new BundlesResponseBuilder();
    }

    public Set<Bundle> getBundle() {
        return this.bundle;
    }

    public void setBundle(Set<Bundle> bundle) {
        this.bundle = bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BundlesResponse)) {
            return false;
        } else {
            BundlesResponse other = (BundlesResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$bundle = this.getBundle();
                Object other$bundle = other.getBundle();
                if (this$bundle == null) {
                    if (other$bundle != null) {
                        return false;
                    }
                } else if (!this$bundle.equals(other$bundle)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BundlesResponse;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $bundle = this.getBundle();
        result = result * 59 + ($bundle == null ? 43 : $bundle.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BundlesResponse(bundle=" + this.getBundle() + ")";
    }

    public BundlesResponse() {
    }

    public static class BundlesResponseBuilder {
        private Set<Bundle> bundleResponseBuilder;

        BundlesResponseBuilder() {
        }

        public BundlesResponseBuilder bundle(Set<Bundle> bundle) {
            this.bundleResponseBuilder = bundle;
            return this;
        }

        @Override
        public String toString() {
            return "BundlesResponse.BundlesResponseBuilder(bundle=" + this.bundleResponseBuilder + ")";
        }
    }
}
