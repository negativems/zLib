package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

public class FireworkEffectSerialization {

    protected FireworkEffectSerialization() {
    }

    public static FireworkEffect JsonToFireworkeffect(JsonObject json) {
        FireworkEffect.Builder builder = FireworkEffect.builder();

        //colors
        JsonArray colors = json.getAsJsonArray("colors");
        for (int j = 0; j < colors.size(); j++) builder.withColor(ColorSerialization.getColor(colors.get(j).getAsJsonObject()));

        //fade colors
        JsonArray fadeColors = json.getAsJsonArray("fade-colors");
        for (int j = 0; j < fadeColors.size(); j++) builder.withFade(ColorSerialization.getColor(colors.get(j).getAsJsonObject()));
        if (json.get("flicker").getAsBoolean()) builder.withFlicker();
        if (json.get("trail").getAsBoolean()) builder.withTrail();
        builder.with(FireworkEffect.Type.valueOf(json.get("type").getAsString()));

        return builder.build();
    }

    public static JsonObject FireworkeffectToJson(FireworkEffect effect) {
        JsonObject jsonObject = new JsonObject();

        //colors
        JsonArray colors = new JsonArray();
        for (Color c : effect.getColors()) colors.add(ColorSerialization.serializeColor(c));
        jsonObject.add("colors", colors);

        //fade colors
        JsonArray fadeColors = new JsonArray();
        for (Color c : effect.getFadeColors()) fadeColors.add(ColorSerialization.serializeColor(c));
        jsonObject.add("fade-colors", fadeColors);
        jsonObject.addProperty("flicker", effect.hasFlicker());
        jsonObject.addProperty("trail", effect.hasTrail());
        jsonObject.addProperty("type", effect.getType().name());

        return jsonObject;
    }

}
