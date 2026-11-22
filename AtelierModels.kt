package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class FaceShape(val displayName: String, val beautyTip: String) {
    OVAL(
        displayName = "Ovalada",
        beautyTip = "Proporciones equilibradas: luce bien tanto el cat-eye clásico como el rubor circular."
    ),
    HEART(
        displayName = "Corazón",
        beautyTip = "Frente amplia y mentón fino: destaca pómulos altos con rubor lifting y labios voluminosos."
    ),
    ROUND(
        displayName = "Redonda",
        beautyTip = "Mejillas suaves: estiliza con contorno angular ascendente y sombras rasgadas fox-eye."
    ),
    DIAMOND(
        displayName = "Diamante",
        beautyTip = "Pómulos protagonistas: suaviza con iluminador en frente y mentón más rubor horizontal."
    ),
    SQUARE(
        displayName = "Cuadrada",
        beautyTip = "Mandíbula definida: armoniza con cejas arqueadas suaves, rubor manzana y gloss central."
    )
}

enum class Undertone(val displayName: String) {
    COOL("Subtono Frío"),
    WARM("Subtono Cálido"),
    NEUTRAL("Subtono Neutro"),
    OLIVE("Subtono Oliva")
}

data class SkinTonePreset(
    val id: String,
    val name: String,
    val undertone: Undertone,
    val skinTone: Color,
    val skinShadow: Color,
    val skinHighlight: Color,
    val naturalLipColor: Color,
    val colorTip: String
)

enum class HairStyle(val displayName: String) {
    LONG_WAVES("Ondas Largas"),
    ASYMMETRIC_BOB("Bob Elegante"),
    AFRO_CURLS("Rizos Volumen"),
    SLEEK_PONY("Coleta Alta"),
    BOHO_BRAIDS("Trenzas Boho"),
    SILKY_BANGS("Lacio con Flequillo"),
    HALF_UP_GLAM("Semirecogido Glam"),
    PIXIE_CHIC("Pixie Texturizado")
}

enum class PortraitPoseMode(val displayName: String) {
    LOOSE_HAIR("Suelto"),
    HIGH_BUN("Moño Alto"),
    SPA_HEADBAND("Diadema Spa")
}

data class PortraitLandmarks(
    val eyeLeft: Offset,
    val eyeRight: Offset,
    val mouthCenter: Offset
)

data class ModelVisualAssets(
    val bodyRes: Int,
    val faceRes: Int,
    val faceLandmarks: PortraitLandmarks,
    val bunRes: Int,
    val bunLandmarks: PortraitLandmarks,
    val spaRes: Int? = null,
    val spaLandmarks: PortraitLandmarks? = null
)

data class WomanModel(
    val id: String,
    val name: String,
    val subtitle: String,
    val skinTone: Color,
    val skinShadow: Color,
    val skinHighlight: Color,
    val naturalLipColor: Color,
    val eyeColor: Color,
    val faceShape: FaceShape,
    val undertone: Undertone,
    val defaultHairStyle: HairStyle,
    val defaultHairColor: Color,
    val visualAssets: ModelVisualAssets? = null,
    val defaultPortraitPose: PortraitPoseMode = PortraitPoseMode.LOOSE_HAIR
)

enum class BrushMotionPattern(
    val title: String,
    val shortHint: String
) {
    CIRCULAR(
        title = "Movimiento Circular",
        shortHint = "Gira suavemente la brocha en círculos sobre la zona"
    ),
    UPWARD_SWEEP(
        title = "Barrido Ascendente",
        shortHint = "Desliza hacia arriba y afuera rumbo a la sien"
    ),
    WINDSHIELD_ARCH(
        title = "Vaivén en Arco",
        shortHint = "Mueve el pincel de lado a lado siguiendo la cuenca"
    ),
    CAT_EYE_FLICK(
        title = "Trazo Firme y Ala",
        shortHint = "Traza al ras de las pestañas y prolonga en diagonal"
    ),
    DABBING_TAPS(
        title = "Toquecitos Suaves",
        shortHint = "Da pequeños toques sin arrastrar el producto"
    ),
    LIP_CONTOUR(
        title = "Perfilado y Relleno",
        shortHint = "Marca el arco de Cupido y desliza hacia las comisuras"
    )
}

enum class BrushTool(
    val displayName: String,
    val motionPattern: BrushMotionPattern,
    val bristleRadiusDp: Float,
    val softness: Float,
    val description: String
) {
    KABUKI(
        displayName = "Brocha Kabuki",
        motionPattern = BrushMotionPattern.CIRCULAR,
        bristleRadiusDp = 24f,
        softness = 0.85f,
        description = "Cerdas densas y suaves para difuminar rubor y polvos en círculos."
    ),
    ANGULAR(
        displayName = "Brocha Angular",
        motionPattern = BrushMotionPattern.UPWARD_SWEEP,
        bristleRadiusDp = 18f,
        softness = 0.75f,
        description = "Corte diagonal que esculpe pómulos y eleva la mirada."
    ),
    BLENDER(
        displayName = "Pincel Difuminador",
        motionPattern = BrushMotionPattern.WINDSHIELD_ARCH,
        bristleRadiusDp = 14f,
        softness = 0.8f,
        description = "Punta esponjosa ideal para degradar sombras sin cortes."
    ),
    PRECISION_LINER(
        displayName = "Pincel Biselado",
        motionPattern = BrushMotionPattern.CAT_EYE_FLICK,
        bristleRadiusDp = 6f,
        softness = 0.25f,
        description = "Punta ultrafina para delineados gráficos, cat-eye y cejas."
    ),
    SPONGE(
        displayName = "Esponja Beauty",
        motionPattern = BrushMotionPattern.DABBING_TAPS,
        bristleRadiusDp = 20f,
        softness = 0.9f,
        description = "Aplica corrector, iluminador y glitter a toquecitos."
    ),
    LIP_BRUSH(
        displayName = "Pincel Labial",
        motionPattern = BrushMotionPattern.LIP_CONTOUR,
        bristleRadiusDp = 8f,
        softness = 0.35f,
        description = "Define el contorno del labio con precisión editorial."
    )
}

enum class MakeupCategory(val displayName: String) {
    BASE_CONTOUR("Base y Luz"),
    EYESHADOW("Sombras"),
    EYELINER("Delineado"),
    BROWS_LASHES("Cejas y Pestañas"),
    BLUSH("Rubor"),
    LIPS("Labios"),
    GEMS("Gemas")
}

enum class BaseContourStyle(val displayName: String) {
    NONE("Sin base extra"),
    SOFT_GLOW("Iluminador Perla"),
    SCULPTED_CONTOUR("Contorno Esculpido"),
    GOLDEN_BRONZER("Bronceado Solar"),
    ARTISTIC_FRECKLES("Pecas Naturales"),
    GLASS_SKIN("Efecto Piel Cristal"),
    EDITORIAL_BAKING("Iluminación Baking")
}

enum class EyeshadowPattern(val displayName: String) {
    NONE("Natural"),
    SOFT_WASH("Velo Suave"),
    GRADIENT_SUNSET("Degradado Bicolor"),
    SMOKY_HALO("Smoky Halo"),
    CUT_CREASE("Cut Crease"),
    GRAPHIC_DUO("Gráfico Editorial"),
    FOXY_WING_SMOKE("Ahumado Foxy"),
    GLITTER_GLAM("Destello Metálico")
}

enum class EyelinerPattern(val displayName: String) {
    NONE("Sin delineado"),
    CLASSIC_THIN("Clásico Fino"),
    CAT_EYE_WING("Cat-Eye Ala"),
    DOUBLE_WING("Doble Ala"),
    SMOKED_LOWER("Ahumado Inferior"),
    EDITORIAL_CONTRAST("Gráfico Flotante"),
    SIREN_INNER("Sirena Lagrimal"),
    REVERSE_CAT("Cat-Eye Invertido")
}

enum class BrowLashStyle(val displayName: String) {
    NATURAL("Natural"),
    LAMINATED_FLUFFY("Cejas Laminadas"),
    DOLL_VOLUME("Pestañas Muñeca"),
    FOX_EYE_LIFT("Efecto Fox-Eye"),
    WISPY_MANGA("Pestañas Manga"),
    ARCHED_SCULPTED("Ceja Arqueada Pro")
}

enum class BlushPattern(val displayName: String) {
    NONE("Natural"),
    APPLE_CIRCULAR("Manzana Circular"),
    LIFTED_TEMPLE("Lifting Ascendente"),
    SUNKISSED_BRIDGE("Efecto Sol"),
    DRAPING_EDITORIAL("Draping Editorial"),
    DOLL_UNDER_EYE("Rubor K-Beauty Alto")
}

enum class LipPattern(val displayName: String) {
    NONE("Natural"),
    VELVET_MATTE("Mate Aterciopelado"),
    MIRROR_GLOSS("Gloss Espejo"),
    OMBRE_GLAM("Ombré Degradado"),
    LINED_90S("Perfilado 90s"),
    BLURRED_KOREAN("Difuminado Suave"),
    METALLIC_FOIL("Satinado Metálico")
}

enum class FaceGemStyle(val displayName: String) {
    NONE("Sin gemas"),
    INNER_PEARLS("Perlas Lagrimal"),
    HALO_CRYSTALS("Arco de Cristales"),
    STAR_DUST("Estrellitas Doradas"),
    GLITTER_TEAR("Destello Lágrima"),
    BUTTERFLY_GLAM("Cristales Sien"),
    UNDER_EYE_STARS("Lluvia de Brillos")
}

enum class FacialZone(val displayName: String) {
    EYES("Ojos y Párpados"),
    CHEEKS("Mejillas y Pómulos"),
    LIPS("Labios"),
    FOREHEAD_NOSE("Frente y Puente"),
    FULL_FACE("Todo el Rostro")
}

data class PaintedBrushStroke(
    val points: List<Offset>,
    val color: Color,
    val brush: BrushTool,
    val alpha: Float,
    val category: MakeupCategory
)

data class MakeupState(
    val baseStyle: BaseContourStyle = BaseContourStyle.NONE,
    val highlighterColor: Color = Color(0xFFFBE9D7),
    val baseIntensity: Float = 0.72f,

    val eyeshadowPattern: EyeshadowPattern = EyeshadowPattern.NONE,
    val eyeshadowPrimary: Color = Color(0xFFC98276),
    val eyeshadowSecondary: Color = Color(0xFF5A2B36),
    val eyeshadowIntensity: Float = 0.82f,

    val eyelinerPattern: EyelinerPattern = EyelinerPattern.NONE,
    val eyelinerColor: Color = Color(0xFF1F161B),

    val browLashStyle: BrowLashStyle = BrowLashStyle.NATURAL,

    val blushPattern: BlushPattern = BlushPattern.NONE,
    val blushColor: Color = Color(0xFFE57482),
    val blushIntensity: Float = 0.65f,

    val lipPattern: LipPattern = LipPattern.NONE,
    val lipColor: Color = Color(0xFFB83B5E),
    val lipSecondaryColor: Color = Color(0xFFF3B5BE),
    val lipIntensity: Float = 0.85f,

    val gemStyle: FaceGemStyle = FaceGemStyle.NONE,

    val paintedStrokes: List<PaintedBrushStroke> = emptyList()
)

enum class OutfitStyle(val displayName: String, val styleTag: String) {
    GALA_GOWN("Vestido Gala Satén", "Gala"),
    STREETWEAR_BLAZER("Blazer & Pantalón Chic", "Urbano"),
    BOHO_FLORAL("Vestido Bohemio", "Bohemio"),
    COUTURE_CORSET("Corsé & Falda Midi", "Editorial"),
    ROMANTIC_TULLE("Vestido Tul Romántico", "Romántico"),
    MINIMAL_JUMPSUIT("Mono Escote Halter", "Minimal"),
    COCKTAIL_MINI("Mini Vestido Drapeado", "Cóctel"),
    DENIM_CROP("Chaqueta & Jeans Alto", "Casual"),
    SILK_SLIP("Vestido Lencero Seda", "Seda"),
    TRENCH_COAT("Gabardina Parisina", "Clásico"),
    MERMAID_SEQUIN("Vestido Sirena Brillo", "Fiesta"),
    ATHLEISURE_SET("Conjunto Sport Chic", "Urbano")
}

enum class ShoeStyle(val displayName: String) {
    STILETTO("Tacones Stiletto"),
    COMBAT_BOOTS("Botas Combat Chic"),
    JEWEL_SANDALS("Sandalias Joya"),
    PLATFORM_SNEAKERS("Tenis Plataforma"),
    POINTED_MULES("Mules Elegantes"),
    OVER_KNEE_BOOTS("Botas Bucaneras"),
    MARY_JANES("Mary Jane Charol"),
    GLADIATOR_HEELS("Sandalias Lace-Up"),
    ESPADRILLE_WEDGES("Cuñas de Verano"),
    ANKLE_BOOTIES("Botines de Tacón")
}

enum class EarringStyle(val displayName: String) {
    NONE("Sin aretes"),
    GOLD_HOOPS("Aros Clásicos"),
    CRYSTAL_DROPS("Cascada Cristales"),
    PEARL_STUDS("Perlas Minimal"),
    BOHO_TASSELS("Aretes Bohemios")
}

enum class NecklaceStyle(val displayName: String) {
    NONE("Sin collar"),
    DIAMOND_CHOKER("Gargantilla Brillo"),
    PENDANT_CHAIN("Cadena con Dije"),
    PEARL_STRAND("Collar de Perlas"),
    LAYERED_CHAINS("Capas Doradas")
}

enum class HeadAccessoryStyle(val displayName: String) {
    NONE("Sin tocado"),
    PEARL_TIARA("Tiara de Perlas"),
    SATIN_HEADBAND("Diadema Satinada"),
    GOLD_CLIPS("Horquillas Doradas")
}

enum class JewelryMetal(val displayName: String, val primary: Color, val highlight: Color) {
    GOLD("Oro Amarillo", Color(0xFFD4A24E), Color(0xFFFFEAB5)),
    SILVER("Plata Platino", Color(0xFFC5CCD4), Color(0xFFF5F8FA)),
    ROSE_GOLD("Oro Rosa", Color(0xFFD69085), Color(0xFFFFDFD9)),
    PEARL("Perla Nácar", Color(0xFFF4EDE6), Color(0xFFFFFFFF))
}

data class WardrobeState(
    val outfitStyle: OutfitStyle = OutfitStyle.SILK_SLIP,
    val outfitColor: Color = Color(0xFF221A20),
    val outfitAccentColor: Color = Color(0xFFD4A24E),
    val shoeStyle: ShoeStyle = ShoeStyle.STILETTO,
    val shoeColor: Color = Color(0xFF23181D),
    val earringStyle: EarringStyle = EarringStyle.PEARL_STUDS,
    val necklaceStyle: NecklaceStyle = NecklaceStyle.PENDANT_CHAIN,
    val headAccessoryStyle: HeadAccessoryStyle = HeadAccessoryStyle.NONE,
    val jewelryMetal: JewelryMetal = JewelryMetal.GOLD
)

enum class ViewZoomMode {
    FACE_CLOSEUP,
    FULL_BODY
}

data class LessonStep(
    val id: String,
    val title: String,
    val instruction: String,
    val proTip: String,
    val requiredBrush: BrushTool,
    val targetZone: FacialZone,
    val targetCategory: MakeupCategory,
    val guideNormalizedPoints: List<Offset>,
    val previewColor: Color,
    val applyStep: (MakeupState) -> MakeupState
)

data class MakeupLesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val difficulty: String,
    val durationLabel: String,
    val accentColor: Color,
    val steps: List<LessonStep>
)

data class StyleLookbook(
    val id: String,
    val title: String,
    val vibeTag: String,
    val description: String,
    val makeupNotes: String,
    val fashionNotes: String,
    val makeup: MakeupState,
    val wardrobe: WardrobeState,
    val suggestedHair: HairStyle
)

data class StrokeFeedback(
    val message: String,
    val isPositive: Boolean,
    val progressGain: Float
)
