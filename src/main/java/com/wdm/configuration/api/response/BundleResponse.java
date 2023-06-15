package com.wdm.configuration.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("PMD.CyclomaticComplexity")
public class BundleResponse {
    private String bundle;
    private Set<AttributeResponse> attributes;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BundleResponse)) {
            return false;
        } else {
            BundleResponse other = (BundleResponse) o;
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

                Object this$$bundleAttributes = this.getAttributes();
                Object other$$bundleAttributes = other.getAttributes();
                if (this$$bundleAttributes == null) {
                    if (other$$bundleAttributes != null) {
                        return false;
                    }
                } else if (!this$$bundleAttributes.equals(other$$bundleAttributes)) {
                    return false;
                }

                return true;
            }
        }
    }

    public boolean canEqual(Object other) {
        return other instanceof BundleResponse;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $bundle = this.getBundle();
        result = result * 59 + ($bundle == null ? 43 : $bundle.hashCode());
        Object $bundleAttributes = this.getAttributes();
        result = result * 59 + ($bundleAttributes == null ? 43 : $bundleAttributes.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BundleResponse(bundle=" + this.getBundle() + ", bundleAttributes=" + this.getAttributes() + ")";
    }

}
