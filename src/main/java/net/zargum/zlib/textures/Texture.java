//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.zargum.zlib.textures;

import com.mojang.authlib.properties.Property;
import lombok.EqualsAndHashCode;
import net.zargum.zlib.utils.item.SkullBuilder;
import net.zargum.zlib.utils.serialize.DocumentSerializable;
import org.bson.Document;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

@EqualsAndHashCode
public final class Texture implements DocumentSerializable {

    public static final Texture DERP_STEVE = new Texture("eyJ0aW1lc3RhbXAiOjE0ODcxNjAxNzE5NzcsInByb2ZpbGVJZCI6IjQzYTgzNzNkNjQyOTQ1MTBhOWFhYjMwZjViM2NlYmIzIiwicHJvZmlsZU5hbWUiOiJTa3VsbENsaWVudFNraW42Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xODdjOThlNzkzN2I4NTg0MWVmMWEzNjEyMzI2ZGE4ODgxZDE2MTVkYWVjNGMzMzQ1OThiM2U1YjUxODUxIn19fQ==", "J6WrGgCPv5YYXuyj/7oisKUACWaPqGfnzI/wIPTDvLhNCiQ7qCEm8Dmu89WKULAy9+mOaslgjQPxSO8b/NL3gAQmOgRjeOz9edCmC6voJpRwVYWME8aYYiD7DK1VgAnsHdecc7SDDc/aTvEb2Dq0rlkF79zi2kjijo9gUm/Hb+qIXUwBdRiuip2b4TKulcqQCVrg8wPzWXc8Jm2A5FzCTUllEbKMo9+9EUeL3yF0krM5uM/HEFTsIff6uQS6Omq8myzG8VCBGIiwtAHFUCUTMYKhA9Oln4gGIkc8ROvuBVvAnuiv0g/aIfmGgY2hy7e+g3DsLUw8Y+ijW2JKjAbxzNZnLtNEBF57uABtBIBznC7yw9LRC1pCcH9IEKth3RU8BlR88ubUG4vIrZGp0P/BdLq/neWj04KdR56nSNs/YG9ghPwXPGieAG8llAZq8vL3hMIw6JyW9qWHtq2Y6lJN//XwYoFgMSHB/JxP1UxmIpevFnc4IlV+AhKyXbdIIsY7RRKif5L7P2sMtHpHA7/QMQmZsRzpXnuJvjcu0OIm0Rju2zsuFGfqeeVKGekecLlcYOC2y0Cg1Rw/prNNjuzKFK5BD7HSozaY1t87B2MYxumXQOkUkGMplTI3hohEU8kL00EgbOAQ7iw8XaKdNBFG7E2wA1J2+57UT+WF4Pq2hMQ=");

    // Nebula
    public static final Texture MINE = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM1NzQ0NGFkZTY0ZWM2Y2VhNjQ1ZWM1N2U3NzU4NjRkNjdjNWZhNjIyOTk3ODZlMDM3OTkzMTdlZTRhZCJ9fX0=");
    public static final Texture ENCHANTMENTS = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc2MmExNWIwNDY5MmEyZTRiM2ZiMzY2M2JkNGI3ODQzNGRjZTE3MzJiOGViMWM3YTlmN2MwZmJmNmYifX19");
    public static final Texture FARM = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFlZjgxZjIwZTM4YTc3YTU1MmU0MGI2ZjYxNTY0ZTk1MmNkZWYzNjYyMDJhZDM0NzU3MjA5YzhjYjZmZjEyNSJ9fX0=");
    public static final Texture CHEST = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZiZDg3Y2Q4YzE0N2U3NmM1N2UxYmI1NDE1YmJjYzU0YjhjNDNjNTYxNzc2NzcyNTczMjNjZDI2OWFlNCJ9fX0=");
    public static final Texture DUNGEON = new Texture("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3YTQyMmRiMzVkMjhjZmI2N2U2YzE2MTVjZGFjNGQ3MzAwNzI0NzE4Nzc0MGJhODY1Mzg5OWE0NGI3YjUyMCJ9fX0=");

    // Worlds
    public static final Texture WORLD_NETHER = new Texture("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUwMDI5MmY0YWZlNTJkMTBmMjk5ZGZiMjYwMzYzMjI4MzA0NTAzMzFlMDAzMDg0YmIyMjAzMzM1MzA2NjRlMSJ9fX0=");
    public static final Texture WORLD_END = new Texture("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY5OTRkNzFiODc1ZjA4N2U2NGRlYTliNGEwYTVjYjlmNGViOWFiMGU4ZDkwNjBkZmRlN2Y2ODAzYmFhMTc3OSJ9fX0=");

    // Blocks
    public static final Texture GRASS = new Texture("eyJ0aW1lc3RhbXAiOjE1NjkyODE3NTE2NTgsInByb2ZpbGVJZCI6IjllMWNiZDg2N2I2NDQyMzRiZmQwNGZmNDk4NTRiYWM5IiwicHJvZmlsZU5hbWUiOiJNSEZfR3Jhc3MiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U0MGYyMzkxZTcxZGY1NmQ4Zjg4MTBlODAzOWZjZWQ1YjJlMzUzOTRmNDBkZTZmZWNmNjQ0YmRlMmZmYjAxN2QifX19");

    // Social media
    public static final Texture TWITTER_LOGO = new Texture("eyJ0aW1lc3RhbXAiOjE1ODE0NjM2MTk1MDEsInByb2ZpbGVJZCI6ImI1ODZkMzhlYjQ0YjQwNDc4NzI3MGIwOGRiYjE0YzU0IiwicHJvZmlsZU5hbWUiOiJNYW5pdGFoIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNjFiZDYyNjEyMzY0MDFlYWQyY2YzZTI4NjBlOTg5NDcwNDM3MTVjMTBjNmZiZWVlMDMzZTdlNzE2NzhmMzAwIn19fQ==", "xIeofbco+oUVJgDv8W6I+Tz0W4LdJ6kJXQ0EJZ5iBVe/wV6g3w4rqz3pUv0iX5I0UQ5mOY9nQ7/z1uBMu/6gjaXMNvYvFTrccZuBR7rxBvM846mnXrXvvkMEQImRae2r7V/BuINfF7j1Uik0Odzb6i9SQGDftBG1mHAmg5tN85KWp9pvayhfu1KU9EpoX7UWD/4g3YcVNWDG9Z0781/2sflwGeww4/x3E4krLSuPAm+CFMTBKau1zWLLkkdUiN5UbIoR898w3iiBQGfGtgQylGioBkBraJKRDe4oZ0RRGaVdOIc633Z8NQjs+AJul6jLC5RqaqZaSMGprh5dhpQ1qnarvOoznUVzJVp3XXup8gw7OfqfN2ndFwdA2ZEpBu1ynIotu53ZuviW4xgw/HTVxrUH+9NKuu7pGvdfVOGkgKc4o/N5or0PXQUasxPxqxiMxhG6Axh30RiL39C2uhNmZnuWVLoLd/bBx+hZm1uX4nXHxi553MND1X/VGeZU/cwIsTb0u3rllsbtqQJFMzfu+9YncnbLCOhmw/z4mBi6LT/cRaXenvoytobjvzSJo3wd2DHaa5SCP6iCeDLv6FK6eBAZmSeVWozS/mcP90/mDYtKiMIJ1P6QTuMYEpkgcNlHHlnBHE/1Pnfw/FmbKjMvBfNjU+o4mewcopunufCJWTI=");
    public static final Texture DISCORD_LOGO = new Texture("eyJ0aW1lc3RhbXAiOjE1ODE0NjQ1NTc3MzYsInByb2ZpbGVJZCI6ImI1ODZkMzhlYjQ0YjQwNDc4NzI3MGIwOGRiYjE0YzU0IiwicHJvZmlsZU5hbWUiOiJNYW5pdGFoIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzUzN2I0MjMwYjFmMjE2MDU0NTUyODgzYjU1MDQwODg3MDAwNDNmZTgzYmM5ZDY0MjlhMTIyMjUzZjQ3ZjJiIn19fQ==", "jV/TpOH23Wiql1Qc4LdO0WTc6E/IzEz8dRbaHZR4MzvfNJY90KPq60fXMxowpzpveVOWn1AU71YSozuCAZYWrRtPB4S131bXVzJpi5EGmuroJnB8/JwKTmwXUkMy997fCiax7ZeeFDIlSwtGMiNkPw++A1bevxQj0PikVGrnHmySb6R3JbNweAC3vCIkfEwIEI4EibzdfTeJA/ACwKL5Y9q7tBF4nbWZ9TO7EBlfTePB/3cn7CnXGuewFwb8U9TguFjPvfI0NeUeGzyCT+h1rXh+7o0SDoShukDcdxYpc2Nf0cM7zX/bQP7CMG5vIedAo9h6+pOHFx4PLhLsp9MOq9GEFNUd463aq5GDyn1q1QtMaSBeIV79epD4k84vFgpwXh04WL0JwG+5IoWWRaQ5KHMCeVBOYo7I1nhtHdEnJMkj5AnPUpcf9fBCDgm2c7Qq4qsXtr4w+MH0zri+U6+bzj/ic+dVTZE7r7fK224jCxYBTIWVn8lRk7aAaFz+ibY4N1EeZDpbrkB7nWFsXj1ebdNar29V8YApVCfAosUstMn2M5/mUvI/ADxBPEKYb0cnbCaL0ANaFgMcRdlA3igDaf9DmBdTPjUIvpqbiP9IVwJeEEm6K8w0a0MGL2v0sqb/nr4pGRX2NVsULN6WuNM6IZWs6WLd/jWJ0JbN8hC8ggc=");

    // Icons
    public static final Texture DOLLAR_SYMBOL = new Texture("eyJ0aW1lc3RhbXAiOjE1ODQxNjIyNTgyNzksInByb2ZpbGVJZCI6IjM4NTkwNTdjOWE3ZTRiMGY4OWRhYjU4NjA0ZTM5MjBkIiwicHJvZmlsZU5hbWUiOiJ2ZW5kZXBhdHJpYSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOGNjMmFkM2ZhYTAyZjMzNTAyNzdiNjAzZTRlYjYxODVlYWJkNDc0MzlkMmRmZDBmNzYyNGU2ODYwNTNmNmFhIn19fQ==", "wT8ERg8o9t7HDo/BSyg7kxJf+8i8q3EQSMpuFYgIlgzKweUAIiwg26d7//r5wJVxqWZfAl3mqlIevK3X66p2GGyvRfIO6/VgP0kGvK1fqaZGZX6Tev5Az+3rktB6Yl+2IFQR1FF77qn8f6kfk6qf8cDokOEp1CVHdZO6qya9qCh1zRlSVOUtaO9RVjX3OiIwKazuQaZNalt9qURspcx8HiXYSQzGHJmUHNHpUkPRZUHH2ToqUyBf2LLHk4OPmKL8b++e88sSTh6ms+5XT2X0A/tqDyTuoSNELYyjyWZDMnb63Pv39GjwGvX0l0VPHYebOP24njEt22h96cTazGTmozhDUQoF+PQ+6E/Yk1jguw5Lx/s83Tny35GDvnOVPCpy/lElgtgSTeo7Z9h4ksc9fDVgMyMCCGm4AONvXz37CccEVAoKJi2slev8Rutb2Py+tuNApEHDoLM2XQx7XdPtCad/C91ncBpBby416ABQ4eQwBvHELus0nYzuv+0dDByvGrDGJceIQoeUdnR97Q/7OJbiFHetlzTGhsGLzaBWNcbtFedOx4sdUMJMfKbY+lgTO4E/NEa6E9oTPz/myIR5lcR6WrKXCFBYJp8dAVMHx1aNLI3XEPjWaf+RADF2pZ/b61UwUhZ8hbkjROSRY0CNJr5wqrX1VEkp2Hw/Of+1ujg=");
    public static final Texture MONEY_BAG = new Texture("eyJ0aW1lc3RhbXAiOjE1ODE0NjQxMTkyMDksInByb2ZpbGVJZCI6ImI1ODZkMzhlYjQ0YjQwNDc4NzI3MGIwOGRiYjE0YzU0IiwicHJvZmlsZU5hbWUiOiJNYW5pdGFoIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ODNlMTI2NGJlZDIzNTdkZjMyMDU0NDQ4MzQ2MzJkNGFhMDQ0ZWUzYzg2OWJiMDBlYjU3Yzg3MWM2YTQ1MTZlIn19fQ==", "LYJN60UnwlEN60okhkOulcxX6ZYwVm/atcTaYgkiyXhYo92EfLwsYrYyCezqhLwVLhYt2QxwFKU63uh/bVlB3kJm9FZYb8NwBNANExX783EGLqvGO9MN0t1F64YB+w5JBJNhYFF168dL8BijrrAhjetzd7hbKbsi5LSx74slSkHnJrcWIrkUgfdFCEKTGwRHqiTqV/xeN5ymNGmm110/UXAUr5g7NVF96S/vYBsYiYDbzlKkPdAUUbODmt1n4B4pqAzknTQAXS4uszSIAKge/9FYjCQZdNLtN2tWMFcCWW0mRy6VqWRgPVcXqqC6R37VpINxrD4tHHXE78RDw1Ee6fCz3CNGC82LxG/jvJSWI6j4upadYHfAdn+jUrj9k1lbYVsoqg9of2vGTLo31oB1VHICstyxhxAunNz3b2YeW0zSn+BwMnVpmOC/rhMQWsWqBnnFOR/g8lI5zTu1M9S1jCQqBZCRU++nyKqJ1jf7xNpMeAHfN/kDq6tKSPGqMZVElpyjqnFORZmYw0cRrvkMWj3tEhtccKQffhlA53zldbCBssNmO6vhpTfwk5Ts6oG4BHaX+gDbMM7vstsn39G2YsWAi8Iq4FrTKhhHduOVFClXzlm8/tLKjz3Y8EI7H7TVB6PgwQ1lnh1C0gG/ASLwzxM/vlvLUt9EG8WpP3EH/80=");
    public static final Texture PRESENT = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjBhZmE0ZmZmZDEwODYzZTc2YzY5OGRhMmM5YzllNzk5YmNmOWFiOWFhMzdkODMxMjg4MTczNDIyNWQzY2EifX19");

    // Solid Colors
    public static final Texture BLACK_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3YTJmMjE4YTZlNmUzOGYyYjU0NWY2YzE3NzMzZjRlZjliYmIyODhlNzU0MDI5NDljMDUyMTg5ZWUifX19");
    public static final Texture DARK_BLUE_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE0NjA1MzAxMmM2OGYyODlhYmNmYjE3YWI4MDQyZDVhZmJhOTVkY2FhOTljOTljMWUwMzYwODg2ZDM1In19fQ==");
    public static final Texture DARK_GREEN_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM5ZTYwMWVkOTE5OGRiYjM0YzUxZGRmMzIzOTI5ZjAxYTVmOTU4YWIxMTEzM2UzZTA0MDdiNjk4MzkzYjNmIn19fQ==");
    public static final Texture DARK_AQUA_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZWY2OTI5ZWJhZjM5NGJjZTJlZTdlYTJhZGJiYjVjODNkN2NlMTdlM2I4NjE1YTkyOGFlZmFiZjg1YiJ9fX0=");
    public static final Texture DARK_RED_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY0ZGMzYzM3NTNiZjViMGI3ZjA4MWNkYjQ5YjgzZDM3NDI4YTEyZTQxODdmNjM0NmRlYzA2ZmFjNTRjZSJ9fX0=");
    public static final Texture DARK_PURPLE_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTMyYWUyY2I4ZDJhZTYxNTE0MWQyYzY1ODkyZjM2NGZjYWRkZjczZmRlYzk5YmUxYWE2ODc0ODYzZWViNWMifX19");
    public static final Texture GOLD_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM0ODg2ZWYzNjJiMmM4MjNhNmFhNjUyNDFjNWM3ZGU3MWM5NGQ4ZWM1ODIyYzUxZTk2OTc2NjQxZjUzZWEzNSJ9fX0=");
    public static final Texture GRAY_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmExN2U5NzAzN2NlMzUzZjg1ZjVjNjVkZjQzNWQyOTQ0OWE4OGRhNDQ0MmU0MzYxY2Y5OWFiYmUxZjg5MmZiIn19fQ==");
    public static final Texture DARK_GRAY_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4ZjMyMzQ2MmZiNDM0ZTkyOGJkNjcyODYzOGM5NDRlZTNkODEyZTE2MmI5YzZiYTA3MGZjYWM5YmY5In19fQ==");
    public static final Texture BLUE_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhZmExNTU1ZTlmODc2NDgxZTNjNDI5OWVjNmU5MWQyMmI0MDc1ZTY3ZTU4ZWY4MGRjZDE5MGFjZTY1MTlmIn19fQ==");
    public static final Texture GREEN_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJkMTQ1YzkzZTVlYWM0OGE2NjFjNmYyN2ZkYWZmNTkyMmNmNDMzZGQ2MjdiZjIzZWVjMzc4Yjk5NTYxOTcifX19");
    public static final Texture AQUA_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDdjNzhmM2VlNzgzZmVlY2QyNjkyZWJhNTQ4NTFkYTVjNDMyMzA1NWViZDJmNjgzY2QzZTgzMDJmZWE3YyJ9fX0=");
    public static final Texture RED_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZkZTNiZmNlMmQ4Y2I3MjRkZTg1NTZlNWVjMjFiN2YxNWY1ODQ2ODRhYjc4NTIxNGFkZDE2NGJlNzYyNGIifX19");
    public static final Texture LIGHT_PURPLE_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUzZGNkYmVlYTM1ZjdlY2IxNjY3NGFjNmZmZWQ3OTA2Nzc1MTM5ZTIyYzc4YmY3Mjk1Mzg2ZDMxOTRlOWY2In19fQ==");
    public static final Texture YELLOW_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY0MTY4MmY0MzYwNmM1YzlhZDI2YmM3ZWE4YTMwZWU0NzU0N2M5ZGZkM2M2Y2RhNDllMWMxYTI4MTZjZjBiYSJ9fX0=");
    public static final Texture WHITE_COLOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY2YTVjOTg5MjhmYTVkNGI1ZDViOGVmYjQ5MDE1NWI0ZGRhMzk1NmJjYWE5MzcxMTc3ODE0NTMyY2ZjIn19fQ==");

    // Menu Icons TODO: Change icons
    public static final Texture COLOR_SELECTOR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIyNzY3MGQxNDg3OTQ5MTUzMDQ4MjdiMGViMDNlZmYyNzNjYTE1M2Y4NzRkYjVlOTA5NGQxY2RiYjYyNThhMiJ9fX0=");
    public static final Texture PIGGY_BANK = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ZGY0MmY0NzdmMjEzZmY1ZTlkN2ZhNWE0Y2M0YTY5ZjIwZDljZWYyYjkwYzRhZTRmMjliZDE3Mjg3YjUifX19");
    public static final Texture LEAVE_MENU = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE0NjE2YWQ0ODgxYzcyNTQzYTIyZDhjYTY3ZDY3OWVjZTdiM2Y4NDI1MDczNmQ5NjJiZDdjMDMwNjk2NCJ9fX0=");

    // Heads
    public static final Texture PANDA = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODAxOGExNzcxZDY5YzExYjhkYWQ0MmNkMzEwMzc1YmEyZDgyNzkzMmIyNWVmMzU3ZjdlNTcyYzFiZDBmOSJ9fX0=");
    public static final Texture POLAR_BEAR = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMzZDA2MDA5NDhiYTM4MDg2NGM3MGM3YWJmM2YwNTc4NGRkOTI3N2Y2MjRiNjY2ZDIwM2IwNTVlYWUzYWU0MSJ9fX0==");
    public static final Texture WHITE_DRAGON = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ3MWNiYzdiZjkxMzcxNDUwMmIwMDE0Mjg5MDBiNDBlNTUyNjZmNTcwODY4MjFhZjNiYzE4MzhhYWE4MTQ2NSJ9fX0=");
    public static final Texture WINNIE_THE_POOH = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFmNzVmZDBiNTY3NmE3YTBmODY1N2FhOWE5ZmZjZDU4Y2Q5ZGU3ODNlZDliYzI2ZTIwM2IyNTRhMDRlYTg0YiJ9fX0=");
    public static final Texture STORMTROOPER = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhNjlhYzY3ZDVjMmZiZTRhYzM0ODZkODFjNWZlYmQ2MmEwZjNmNzVhNjg3NDQyZTYzYjU5NTI4ODg2MTY3NyJ9fX0=");
    public static final Texture REINDEER = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM2MDNmZTcwNGE5ZDA0OThlOWViZDlkMmE3N2JlNjAxYTdlODVlYmE2NmY2NTBkYjk0MTk5ZWJlN2Y3NWI1NSJ9fX0=");
    public static final Texture OFFICER_MALE = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRmY2U3ODUxODk2ZTc4OWU2ODJhY2Y2MjQwYzZkNTZhMTQ0Y2Y4M2E2ZTAyN2M5MDI0MGI2MDJiM2I3OTk2NiJ9fX0=");
    public static final Texture OFFICER_FEMALE = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTMxMWMxNjAwYWZkYWJiMTk1ZTY0ZmM4ZTRjNTE3MjIyNWU1MDZjNzE1MjYwYzNmODI3ZjAwOTNiMzk4OWE5NCJ9fX0=");
    public static final Texture HALLOWEEN_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkZWUyZTgxNGNkNGI1ZTkzM2Q4ZjJiMzBhMGJjMmE1ODM1NTJjYmUzZGFlNTgxYmVjMDlmYmI2ZWIwN2UifX19");
    public static final Texture DOG_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODM5Mjg5Mzk3MiwKICAicHJvZmlsZUlkIiA6ICI4ZTA3MWFjNmE5ZDE0ZWJhODQ0ZDUzODYxNWFiMWUxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPbGluZWsxMjIiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNhOTlhOGE4OGRmMjBkZGM0N2Y2ZTIyMWVmN2EyNjAxMjc0ZTYzMWQ2NDFlNWZmOWY2MGZlZTVlNTI1NTQ2OSIKICAgIH0KICB9Cn0=");
    public static final Texture HAMBURGUER_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODYxMzY2ODg2MywKICAicHJvZmlsZUlkIiA6ICIzMDRjZGU4NTBlMzc0YTY5YjAyN2E1ZTIxOTVmM2EzMyIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDAwMDAwMDAyIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ExMDc1M2Q5MDc0ZjM5ZTM3MTZiMWUxOWVhZjI2MGVjM2I1MjQyM2UyOTM2MGM1ODg1NTBlYjRlMmRhNTFhMTciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
    public static final Texture PENGUIN_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODYxMzkwOTY5MiwKICAicHJvZmlsZUlkIiA6ICIyY2NkMGJiMjgxMjE0MzYxODAzYTk0NWI4ZjA2NDRhYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSdWFuIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U0MjA1OTI0OWMyYmQxZGU5NWJkYjNmMjE5NGZkMDFlZjhjMmVlOTI2NjczN2RhYjJmMTQ2NjYyMmJhMGZlM2EiCiAgICB9CiAgfQp9");
    public static final Texture MONEY_HEIST_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk5MzY0M2IwODJiMWQ3NjY0NWNiNjVjM2JjNWM3NTdmMTczNDYzMzNiMGNhZjM4NTgwY2U1M2QyNDhlY2Y3ZCJ9fX0=");
    public static final Texture SQUIRREL_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVmNmVkNzY2MzY4YTllMDdiM2IzMTZhODllZmNiOTM3MThjODY2ZmE5MmI5YWFhYTRlYjY4ZGZlNDc2YjFkNSJ9fX0=");
    public static final Texture SQUIRREL_KING_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODY0OTMzMzI4MSwKICAicHJvZmlsZUlkIiA6ICI3NTA1OWUwNmMwNzU0ZmIxYmFlMmQyNmVmMjk5NDcyYyIsCiAgInByb2ZpbGVOYW1lIiA6ICIyT08wIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVlNThiMmRmODc0NTFkMzViNDZlYTM4YmM1NGNkOTFjMzk4MTVmZmI0MTlhYWRmODMwYTkxZDM2ZGUxZWNkNGIiCiAgICB9CiAgfQp9");
    public static final Texture DEADPOOL_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVkYzQ5MmVkM2U3NDdiZDNjNjNhYWI1MjFmMzVjMTQxNDc3MTBiYmM3MmVkNjgxZmRlNTgxYzI3NjVlZmEifX19");
    public static final Texture PINK_PANTHER_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODYxNjI1Njc4MSwKICAicHJvZmlsZUlkIiA6ICJiM2ZhNTE1ZDdjNTU0YTY2OGFkOTE5OGU0MWI1MTNmMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJkZmdzdGVhbSIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80N2M4NTY1OWMzNzQ2MDI3OTFlMWFmYzBiMzA2NWEyZTQ4ODhjNWI3ZmRiOWM3ZmJlOTMwNzM3MTA4NGYzYzdkIgogICAgfQogIH0KfQ==");
    public static final Texture SPONGEBOB_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3MWVmMzlhZjRlMzNlYmNmNjlhNGJlNjM3OTU0M2M1MDE1YWFlYzc2YmFiNmZjM2Q4NjJhNzVkZmUzYzQ3In19fQ==");
    public static final Texture GARFIELD_HEAD = new Texture("ewogICJ0aW1lc3RhbXAiIDogMTU4ODYxNzk3MzE0NywKICAicHJvZmlsZUlkIiA6ICJkZTc5YWY5YmMwZTU0ZDJhOWY0YjBhNmNiOTFhNjFhMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUb21hdG90b3dubiIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZjY0ODQ4MGVhNDNjZGVhNmVkZjU5NTFlNGQyMTZhM2FiNTAwOTc4MzU1YTgwNDI3ODY4ZDg0MDM0YzhkMDEiCiAgICB9CiAgfQp9");
    public static final Texture SUBZERO_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU5YTUyMzE0MDgxNWRmYzhiNWRmNzczMzRlNWRiNjdjMzU1Njk3OGQ2N2YxYjAxMTg3NTQ4ZTMyNWRlNTY5In19fQ==");
    public static final Texture SCORPION_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM5NDM4ZThlNGQ5MTQ4YTBiYTkyYTYzNDhjYWM0OWYyMWFmMWUzODNhYjM1Mjk3MzlkYmE3YTI1ZDA2ZSJ9fX0");
    public static final Texture NINJA_TURTLE_RED_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTU0NjM4OWJjZDJhMzJhNWE1OGY2ZTJlMzc5OTA0ZGIwZDE2N2MyM2JhODgwZDUxOGQ0ZTFhMjI1YjU3ODIwIn19fQ==");
    public static final Texture NINJA_TURTLE_ORANGE_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5ZWUxNTVjMzE2NjNlN2E0YzNkMjkwNzZlOGY5Y2ZhMTgxOWFhMmJhZmFhMzMzYmQwYTM2MWZkYTFkYWNiIn19fQ");
    public static final Texture NINJA_TURTLE_PURPLE_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M1YjI2ZGY1MDk5OGE5NWNlODA3ODg1ZjllNWZkZjI0YTY1ZDQ3MWY0OTdhNWYxM2UwY2NjNzM3M2ViNTQifX19");
    public static final Texture NINJA_TURTLE_BLUE_HEAD = new Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdhOWYyNTZlMDMzN2JlODVmYzg4YzE3NmZkMTUwNzdhOTQyZjk1YTdjNTljMTQwNzFjNjYyM2I1NTZlMGU4In19fQ==");

    private final String name;
    private final String value;
    private final String signature;

    public Texture(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public Texture(String value, String signature) {
        this(null, value, signature);
    }

    public Texture(String value) {
        this(value, null);
    }

    public static Texture getPlayerTexture(Player player) {
        try {
            Property property = ((CraftPlayer)player).getProfile().getProperties().get("textures").iterator().next();
            return new Texture(player.getName(), property.getValue(), property.getSignature());
        } catch (NoSuchElementException var2) {
            return DERP_STEVE;
        }
    }

    public static Texture getTexture(UUID uniqueId) {
        Scanner scanner = null;

        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL("https://api.minetools.eu/profile/" + uniqueId.toString().replace("-", ""))).openConnection();
            connection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            if (connection.getResponseCode() != 200) {
                return null;
            } else {
                StringBuilder builder = new StringBuilder();
                scanner = new Scanner(connection.getInputStream());

                while(scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }

                connection.disconnect();
                Document raw = (Document)Document.parse(builder.toString()).get("raw");
                Document property = raw.getList("properties", Document.class).get(0);
                Texture var6 = new Texture(raw.getString("name"), property.getString("value"), property.getString("signature"));
                return var6;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        } finally {
            if (scanner != null) {
                scanner.close();
            }

        }
    }

    public Texture(Document document) {
        this(document.getString("name"), document.getString("value"), document.getString("signature"));
    }

    public Document serialize() {
        return (new Document("name", this.name)).append("value", this.value).append("signature", this.signature);
    }

    public SkullBuilder asSkullBuilder() {
        SkullBuilder builder = new SkullBuilder();
        if (name != null) builder.setDisplayName(name);
        return builder.setTexture(this.value);
    }

    public ItemStack asSkull() {
        return this.asSkullBuilder().build();
    }

    public Property asProperty() {
        return new Property("textures", this.value, this.signature);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }
}
