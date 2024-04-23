package dev.isnow.betterkingdoms.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Converter
public class AdvancedLocationConverter implements AttributeConverter<Location, String> {
    @Override
    public String convertToDatabaseColumn(Location location) {
        return location.getWorld().getName() + ";" +
                location.getX() + ";" +
                location.getY() + ";" +
                location.getZ() + ";" +
                location.getYaw() + ";" +
                location.getPitch() + ";";
    }

    @Override
    public Location convertToEntityAttribute(String s) {
        final String[] split = s.split(";");

        final World world = Bukkit.getWorld(split[0]);

        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);

        final float yaw = Float.parseFloat(split[4]);
        final float pitch = Float.parseFloat(split[5]);

        if(world != null) {
            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return new Location(Bukkit.getWorlds().get(0), x, y, z, yaw, pitch);
        }
    }
}
