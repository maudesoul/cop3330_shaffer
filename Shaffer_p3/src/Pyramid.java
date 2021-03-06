public class Pyramid extends Shape3D {
    private final double base, height, width;

    public Pyramid(double base, double width, double height) {
        this.base = base;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getName() {
        return "pyramid";
    }

    @Override
    public double getArea() {
        double pyramidSurfaceArea;
        pyramidSurfaceArea = (base * width) + (base * Math.sqrt((width / 2) * (width / 2) + height * height)) + (width * Math.sqrt(((base / 2) * (base / 2)) + height * height));       // SA = (base * width) + base * sqrt[(width/2)^2 + height^2] + width*sqrt[(base/2)^2 + height^2]
        return pyramidSurfaceArea;
    }

    @Override
    public double getVolume() {
        double pyramidVolume;
        pyramidVolume = (base * width * height) / 3;        // V = (base * width * height) / 3
        return pyramidVolume;
    }
}
