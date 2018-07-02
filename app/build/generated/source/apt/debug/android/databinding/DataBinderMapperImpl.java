
package android.databinding;
import com.experimental.scrolling.BR;
class DataBinderMapperImpl extends android.databinding.DataBinderMapper {
    public DataBinderMapperImpl() {
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case com.experimental.scrolling.R.layout.activity_scrolling:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/activity_scrolling_0".equals(tag)) {
                            return new com.experimental.scrolling.databinding.ActivityScrollingBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for activity_scrolling is invalid. Received: " + tag);
                }
                case com.experimental.scrolling.R.layout.planet:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/planet_0".equals(tag)) {
                            return new com.experimental.scrolling.databinding.PlanetBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for planet is invalid. Received: " + tag);
                }
        }
        return null;
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    @Override
    public int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -2103620357: {
                if(tag.equals("layout/activity_scrolling_0")) {
                    return com.experimental.scrolling.R.layout.activity_scrolling;
                }
                break;
            }
            case -178991986: {
                if(tag.equals("layout/planet_0")) {
                    return com.experimental.scrolling.R.layout.planet;
                }
                break;
            }
        }
        return 0;
    }
    @Override
    public String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"planet"};
    }
}