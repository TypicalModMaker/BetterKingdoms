package dev.isnow.betterkingdoms.util.converter;

import jakarta.persistence.AttributeConverter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ChunkConverter implements AttributeConverter<List<Chunk>, String> {
    @Override
    public String convertToDatabaseColumn(List<Chunk> chunkList) {
        StringBuilder builder = new StringBuilder();
        for(Chunk chunk : chunkList) {
            builder.append(chunk.getWorld().getName()).append(";").append(chunk.getX()).append(";").append(chunk.getZ()).append(" ");
        }

        // Remove last space
        return builder.substring(builder.length() - 1);
    }

    @Override
    public List<Chunk> convertToEntityAttribute(String s) {
        final String[] split = s.split(" ");

        final List<Chunk> output = new ArrayList<>();

        for(String chunkSplit : split) {
            final String[] chunk = chunkSplit.split(";");

            final World world = Bukkit.getWorld(chunk[0]);

            final int chunkX = Integer.parseInt(chunk[1]);
            final int chunkZ = Integer.parseInt(chunk[2]);

            if(world != null) {
                output.add(world.getChunkAt(chunkX, chunkZ));
            } else {
                output.add(Bukkit.getWorlds().get(0).getChunkAt(chunkX, chunkZ));
            }
        }

        return output;
    }
}
