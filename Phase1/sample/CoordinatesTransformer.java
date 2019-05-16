package sample;

//Projects 3d to 2d.
public class CoordinatesTransformer {

    private  double scale;
    private double originXForOther;
    private double originYForOther;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getOriginXForOther() {
        return originXForOther;
    }

    public void setOriginXForOther(double originXForOther) {
        this.originXForOther = originXForOther;
    }

    public double getOriginYForOther() {
        return originYForOther;
    }

    public void setOriginYForOther(double originYForOther) {
        this.originYForOther = originYForOther;
    }

    // xAxis coordinate
    public double modelToOtherX(double x) {
        return this.originXForOther + getModelToOtherDistance(x);
    }

    // yAxis coordinate
    public double modelToOtherY(double y) {
        return this.originYForOther + getModelToOtherDistance(y);
    }

    // z coordinates
    public double getModelToOtherDistance(double distance) {
        return distance / scale;
    }

}
