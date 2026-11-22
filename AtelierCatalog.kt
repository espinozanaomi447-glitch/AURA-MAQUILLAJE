package com.example.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.model.*

object AtelierCatalog {

    private val spaHeadbandLandmarks = PortraitLandmarks(
        eyeLeft = Offset(0.366f, 0.461f),
        eyeRight = Offset(0.637f, 0.461f),
        mouthCenter = Offset(0.501f, 0.628f)
    )

    private val sofiaAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_sofia_body,
        faceRes = R.drawable.model_sofia_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.409f, 0.399f),
            eyeRight = Offset(0.650f, 0.399f),
            mouthCenter = Offset(0.529f, 0.565f)
        ),
        bunRes = R.drawable.model_sofia_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.350f, 0.476f),
            eyeRight = Offset(0.674f, 0.475f),
            mouthCenter = Offset(0.512f, 0.623f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val camilaAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_camila_body,
        faceRes = R.drawable.model_camila_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.396f, 0.395f),
            eyeRight = Offset(0.657f, 0.393f),
            mouthCenter = Offset(0.527f, 0.561f)
        ),
        bunRes = R.drawable.model_camila_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.350f, 0.490f),
            eyeRight = Offset(0.689f, 0.487f),
            mouthCenter = Offset(0.519f, 0.636f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val zaraAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_zara_body,
        faceRes = R.drawable.model_zara_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.390f, 0.405f),
            eyeRight = Offset(0.631f, 0.404f),
            mouthCenter = Offset(0.510f, 0.571f)
        ),
        bunRes = R.drawable.model_zara_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.354f, 0.479f),
            eyeRight = Offset(0.684f, 0.478f),
            mouthCenter = Offset(0.519f, 0.627f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val valentinaAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_valentina_body,
        faceRes = R.drawable.model_valentina_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.397f, 0.406f),
            eyeRight = Offset(0.659f, 0.406f),
            mouthCenter = Offset(0.528f, 0.568f)
        ),
        bunRes = R.drawable.model_valentina_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.369f, 0.480f),
            eyeRight = Offset(0.684f, 0.478f),
            mouthCenter = Offset(0.527f, 0.626f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val meiAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_mei_body,
        faceRes = R.drawable.model_mei_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.397f, 0.405f),
            eyeRight = Offset(0.657f, 0.403f),
            mouthCenter = Offset(0.527f, 0.573f)
        ),
        bunRes = R.drawable.model_mei_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.370f, 0.480f),
            eyeRight = Offset(0.700f, 0.478f),
            mouthCenter = Offset(0.535f, 0.629f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val elenaAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_elena_body,
        faceRes = R.drawable.model_elena_face,
        faceLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.395f, 0.395f),
            eyeRight = Offset(0.652f, 0.394f),
            mouthCenter = Offset(0.523f, 0.558f)
        ),
        bunRes = R.drawable.model_elena_bun,
        bunLandmarks = PortraitLandmarks(
            eyeLeft = Offset(0.361f, 0.482f),
            eyeRight = Offset(0.691f, 0.480f),
            mouthCenter = Offset(0.526f, 0.624f)
        ),
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    private val noaAssets = ModelVisualAssets(
        bodyRes = R.drawable.model_camila_body,
        faceRes = R.drawable.model_noa_spa,
        faceLandmarks = spaHeadbandLandmarks,
        bunRes = R.drawable.model_camila_bun,
        bunLandmarks = camilaAssets.bunLandmarks,
        spaRes = R.drawable.model_noa_spa,
        spaLandmarks = spaHeadbandLandmarks
    )

    val skinTones: List<SkinTonePreset> = listOf(
        SkinTonePreset(
            id = "porcelain_cool",
            name = "Porcelana",
            undertone = Undertone.COOL,
            skinTone = Color(0xFFF6D9CB),
            skinShadow = Color(0xFFE0B4A2),
            skinHighlight = Color(0xFFFFEFE7),
            naturalLipColor = Color(0xFFDA888E),
            colorTip = "Brillan los rosas pétalo, malvas, iluminador perla y plata."
        ),
        SkinTonePreset(
            id = "ivory_warm",
            name = "Marfil Cálido",
            undertone = Undertone.WARM,
            skinTone = Color(0xFFEFCEB3),
            skinShadow = Color(0xFFD2A787),
            skinHighlight = Color(0xFFFFE7D2),
            naturalLipColor = Color(0xFFCF7E7A),
            colorTip = "Favorecen los tonos durazno, champán, oro rosa y marrón espresso."
        ),
        SkinTonePreset(
            id = "beige_neutral",
            name = "Beige Neutro",
            undertone = Undertone.NEUTRAL,
            skinTone = Color(0xFFE3B898),
            skinShadow = Color(0xFFC2906F),
            skinHighlight = Color(0xFFF6D6BD),
            naturalLipColor = Color(0xFFC67A75),
            colorTip = "Versátil: luce increíble tanto con rojos clásicos como con nudes caramelo."
        ),
        SkinTonePreset(
            id = "olive_gold",
            name = "Oliva Dorada",
            undertone = Undertone.OLIVE,
            skinTone = Color(0xFFD9A682),
            skinShadow = Color(0xFFB77E59),
            skinHighlight = Color(0xFFF0C6A8),
            naturalLipColor = Color(0xFFC07671),
            colorTip = "Resaltan los tonos tierra, bronce solar, esmeralda y oro amarillo."
        ),
        SkinTonePreset(
            id = "honey_tan",
            name = "Trigueña Miel",
            undertone = Undertone.WARM,
            skinTone = Color(0xFFC58C64),
            skinShadow = Color(0xFF9F6843),
            skinHighlight = Color(0xFFE0AE8A),
            naturalLipColor = Color(0xFFB36863),
            colorTip = "Destacan el terracota, coral vibrante, dorado atardecer y gloss miel."
        ),
        SkinTonePreset(
            id = "caramel_radiant",
            name = "Caramelo",
            undertone = Undertone.NEUTRAL,
            skinTone = Color(0xFF9F6748),
            skinShadow = Color(0xFF7B482E),
            skinHighlight = Color(0xFFBF8565),
            naturalLipColor = Color(0xFFA25D5A),
            colorTip = "Espectacular con berry vino, rubor frambuesa y sombras cobrizas."
        ),
        SkinTonePreset(
            id = "bronze_deep",
            name = "Bronce Profundo",
            undertone = Undertone.WARM,
            skinTone = Color(0xFF814E34),
            skinShadow = Color(0xFF5E331E),
            skinHighlight = Color(0xFFA06B4F),
            naturalLipColor = Color(0xFF935052),
            colorTip = "Resaltan los pigmentos saturados, oro solar, fucsia y esmeralda."
        ),
        SkinTonePreset(
            id = "ebony_luminous",
            name = "Ébano",
            undertone = Undertone.COOL,
            skinTone = Color(0xFF633B27),
            skinShadow = Color(0xFF452414),
            skinHighlight = Color(0xFF875840),
            naturalLipColor = Color(0xFF87494D),
            colorTip = "Contrastes editoriales con lila, perlas luminosas, rubí y platino."
        )
    )

    val eyeColors: List<Pair<String, Color>> = listOf(
        "Avellana" to Color(0xFF5D6B44),
        "Azul Zafiro" to Color(0xFF3A6A96),
        "Verde Esmeralda" to Color(0xFF2D7358),
        "Café Miel" to Color(0xFF6B4226),
        "Ámbar Dorado" to Color(0xFF8C5B23),
        "Oscuro Profundo" to Color(0xFF261814),
        "Gris Acero" to Color(0xFF62727F),
        "Violeta Editorial" to Color(0xFF6A4887)
    )

    val models: List<WomanModel> = listOf(
        WomanModel(
            id = "sofia",
            name = "Sofía",
            subtitle = "Oliva dorada • Ondas miel",
            skinTone = skinTones[3].skinTone,
            skinShadow = skinTones[3].skinShadow,
            skinHighlight = skinTones[3].skinHighlight,
            naturalLipColor = skinTones[3].naturalLipColor,
            eyeColor = eyeColors[0].second,
            faceShape = FaceShape.OVAL,
            undertone = Undertone.OLIVE,
            defaultHairStyle = HairStyle.LONG_WAVES,
            defaultHairColor = Color(0xFF3A231B),
            visualAssets = sofiaAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "camila",
            name = "Camila",
            subtitle = "Porcelana • Lacio sedoso",
            skinTone = skinTones[0].skinTone,
            skinShadow = skinTones[0].skinShadow,
            skinHighlight = skinTones[0].skinHighlight,
            naturalLipColor = skinTones[0].naturalLipColor,
            eyeColor = eyeColors[3].second,
            faceShape = FaceShape.HEART,
            undertone = Undertone.COOL,
            defaultHairStyle = HairStyle.SILKY_BANGS,
            defaultHairColor = Color(0xFF3A231B),
            visualAssets = camilaAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "zara",
            name = "Zara",
            subtitle = "Ébano • Rizos volumen",
            skinTone = skinTones[7].skinTone,
            skinShadow = skinTones[7].skinShadow,
            skinHighlight = skinTones[7].skinHighlight,
            naturalLipColor = skinTones[7].naturalLipColor,
            eyeColor = eyeColors[5].second,
            faceShape = FaceShape.DIAMOND,
            undertone = Undertone.COOL,
            defaultHairStyle = HairStyle.AFRO_CURLS,
            defaultHairColor = Color(0xFF1A1314),
            visualAssets = zaraAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "valentina",
            name = "Valentina",
            subtitle = "Trigueña miel • Bob ondulado",
            skinTone = skinTones[4].skinTone,
            skinShadow = skinTones[4].skinShadow,
            skinHighlight = skinTones[4].skinHighlight,
            naturalLipColor = skinTones[4].naturalLipColor,
            eyeColor = eyeColors[3].second,
            faceShape = FaceShape.SQUARE,
            undertone = Undertone.WARM,
            defaultHairStyle = HairStyle.ASYMMETRIC_BOB,
            defaultHairColor = Color(0xFF3A231B),
            visualAssets = valentinaAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "mei",
            name = "Mei",
            subtitle = "Marfil cálido • Bob editorial",
            skinTone = skinTones[1].skinTone,
            skinShadow = skinTones[1].skinShadow,
            skinHighlight = skinTones[1].skinHighlight,
            naturalLipColor = skinTones[1].naturalLipColor,
            eyeColor = eyeColors[5].second,
            faceShape = FaceShape.HEART,
            undertone = Undertone.WARM,
            defaultHairStyle = HairStyle.ASYMMETRIC_BOB,
            defaultHairColor = Color(0xFF18151A),
            visualAssets = meiAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "elena",
            name = "Elena",
            subtitle = "Beige neutro • Reflejos dorados",
            skinTone = skinTones[2].skinTone,
            skinShadow = skinTones[2].skinShadow,
            skinHighlight = skinTones[2].skinHighlight,
            naturalLipColor = skinTones[2].naturalLipColor,
            eyeColor = eyeColors[6].second,
            faceShape = FaceShape.DIAMOND,
            undertone = Undertone.NEUTRAL,
            defaultHairStyle = HairStyle.LONG_WAVES,
            defaultHairColor = Color(0xFF8C7A6B),
            visualAssets = elenaAssets,
            defaultPortraitPose = PortraitPoseMode.LOOSE_HAIR
        ),
        WomanModel(
            id = "noa",
            name = "Noa",
            subtitle = "Marfil rosado • Diadema Spa",
            skinTone = skinTones[0].skinTone,
            skinShadow = skinTones[0].skinShadow,
            skinHighlight = skinTones[0].skinHighlight,
            naturalLipColor = skinTones[0].naturalLipColor,
            eyeColor = eyeColors[3].second,
            faceShape = FaceShape.OVAL,
            undertone = Undertone.NEUTRAL,
            defaultHairStyle = HairStyle.SILKY_BANGS,
            defaultHairColor = Color(0xFF191417),
            visualAssets = noaAssets,
            defaultPortraitPose = PortraitPoseMode.SPA_HEADBAND
        )
    )

    val hairColors = listOf(
        Color(0xFF191417), // Negro Azabache
        Color(0xFF3A231B), // Castaño Espresso
        Color(0xFF6B3E26), // Chocolate Cálido
        Color(0xFF994228), // Cobrizo
        Color(0xFFD4A359), // Rubio Miel
        Color(0xFFE8D5B7), // Rubio Platino
        Color(0xFF732544), // Borgoña Editorial
        Color(0xFFD995A8), // Rosa Pastel
        Color(0xFF475878), // Azul Medianoche
        Color(0xFF8C7A6B)  // Cenizo Natural
    )

    val eyeshadowPalettes = listOf(
        Pair(Color(0xFFD8A48F), Color(0xFF754638)), // Tierra Cálida
        Pair(Color(0xFFE7A9B8), Color(0xFF8B3355)), // Rosa Aterciopelado
        Pair(Color(0xFF9FA5B5), Color(0xFF262634)), // Smoky Nocturno
        Pair(Color(0xFFF0C674), Color(0xFF9C5427)), // Dorado Atardecer
        Pair(Color(0xFF8BC4B0), Color(0xFF1F5446)), // Esmeralda Editorial
        Pair(Color(0xFFC4A7E7), Color(0xFF563480)), // Lila Lavanda
        Pair(Color(0xFF8CB8E8), Color(0xFF204878)), // Zafiro Pasarela
        Pair(Color(0xFFF5B792), Color(0xFFB5442E)), // Terracota Fuego
        Pair(Color(0xFFEBE4DC), Color(0xFF5B4A44)), // Neutro Capuchino
        Pair(Color(0xFFF492B4), Color(0xFF991A47)), // Fucsia Pop
        Pair(Color(0xFFD8D0C0), Color(0xFF3E4B3C)), // Oliva Champagne
        Pair(Color(0xFFF5D88B), Color(0xFF2E2228))  // Oro & Carbón
    )

    val eyelinerColors = listOf(
        Color(0xFF181316), // Negro Carbón
        Color(0xFF4F2F21), // Café Espresso
        Color(0xFF1F3B6E), // Azul Medianoche
        Color(0xFF7A1E3A), // Vino Burdeos
        Color(0xFFD1A04B), // Dorado Metálico
        Color(0xFFFDFBF9), // Blanco Editorial
        Color(0xFF1E6350), // Esmeralda
        Color(0xFF6B3E8C), // Violeta Eléctrico
        Color(0xFFB56E45), // Bronce Cobrizo
        Color(0xFFB5BFC9)  // Plata Cromada
    )

    val blushColors = listOf(
        Color(0xFFEA8C7D), // Durazno Suave
        Color(0xFFE67895), // Rosa Pétalo
        Color(0xFFC95A49), // Terracota Solar
        Color(0xFFB8335F), // Frambuesa
        Color(0xFFF07167), // Coral Vibrante
        Color(0xFFD68FA8), // Malva Frío
        Color(0xFFA84255), // Ciruela Profundo
        Color(0xFFF29F7C), // Albaricoque
        Color(0xFFDA5C7E)  // Rosa Fresa
    )

    val lipColors = listOf(
        Pair(Color(0xFFB51E34), Color(0xFFED7685)), // Rojo Rubí
        Pair(Color(0xFFC27968), Color(0xFFEAB9AA)), // Nude Caramelo
        Pair(Color(0xFF9E4C6C), Color(0xFFDBA4B8)), // Malva Elegante
        Pair(Color(0xFF6B1835), Color(0xFFBF5478)), // Berry Vino
        Pair(Color(0xFFE0574F), Color(0xFFF6A192)), // Coral Beso
        Pair(Color(0xFFDF5C91), Color(0xFFFAC0D7)), // Rosa Pop
        Pair(Color(0xFF8C463B), Color(0xFFC77F72)), // Canela 90s
        Pair(Color(0xFF4C1628), Color(0xFF943C58)), // Cereza Oscura
        Pair(Color(0xFFD9877D), Color(0xFFFAD3CC)), // Rosa Nude Miel
        Pair(Color(0xFFC93B6B), Color(0xFFF5A9C1))  // Magenta Seda
    )

    val highlighterColors = listOf(
        Color(0xFFFFF1E0), // Perla Champán
        Color(0xFFF5D58E), // Oro Solar
        Color(0xFFFADCE8), // Diamante Rosa
        Color(0xFFE8F4FF), // Hielo Glaciar
        Color(0xFFEBC09C), // Bronce Luminoso
        Color(0xFFEBD9FA)  // Holográfico Lila
    )

    val outfitColors = listOf(
        Color(0xFF221A20), // Negro Clásico
        Color(0xFFA61C3C), // Rojo Gala
        Color(0xFF1D5B4C), // Esmeralda
        Color(0xFFEAD7C5), // Champán Marfil
        Color(0xFF2C4268), // Azul Noche
        Color(0xFFD995A8), // Rosa Pastel
        Color(0xFFB87846), // Terracota Boho
        Color(0xFF8B7BA8), // Lila Lavanda
        Color(0xFF3E5E84), // Azul Denim
        Color(0xFF6A1D32), // Borgoña Vino
        Color(0xFFC9A154), // Dorado Satén
        Color(0xFFEDE7E1)  // Blanco Puro
    )

    val shoeColors = listOf(
        Color(0xFF1E171B), // Negro Charol
        Color(0xFFD4A24E), // Dorado Fiesta
        Color(0xFFA61C3C), // Rojo Pasión
        Color(0xFFF5EFEA), // Blanco Urbano
        Color(0xFFD9B29C), // Nude Elongador
        Color(0xFFC5CCD4), // Plata Espejo
        Color(0xFF6A3F2B), // Camel Cuero
        Color(0xFFE3A1B4), // Rosa Satinado
        Color(0xFF1D5B4C), // Esmeralda
        Color(0xFF472536)  // Ciruela
    )

    fun buildCustomModel(
        faceShape: FaceShape,
        skin: SkinTonePreset,
        eyeColor: Color,
        hairStyle: HairStyle,
        hairColor: Color
    ): WomanModel {
        val matchedAssets = when (skin.id) {
            "porcelain_cool" -> camilaAssets
            "ivory_warm" -> meiAssets
            "beige_neutral" -> elenaAssets
            "olive_gold" -> sofiaAssets
            "honey_tan" -> valentinaAssets
            "caramel_radiant", "bronze_deep", "ebony_luminous" -> zaraAssets
            else -> sofiaAssets
        }
        val pose = when (hairStyle) {
            HairStyle.SLEEK_PONY, HairStyle.BOHO_BRAIDS, HairStyle.PIXIE_CHIC, HairStyle.HALF_UP_GLAM ->
                PortraitPoseMode.HIGH_BUN
            else -> PortraitPoseMode.LOOSE_HAIR
        }
        return WomanModel(
            id = "custom_${faceShape.name}_${skin.id}",
            name = "Tu Modelo",
            subtitle = "${skin.name} (${skin.undertone.displayName}) • Rostro ${faceShape.displayName}",
            skinTone = skin.skinTone,
            skinShadow = skin.skinShadow,
            skinHighlight = skin.skinHighlight,
            naturalLipColor = skin.naturalLipColor,
            eyeColor = eyeColor,
            faceShape = faceShape,
            undertone = skin.undertone,
            defaultHairStyle = hairStyle,
            defaultHairColor = hairColor,
            visualAssets = matchedAssets,
            defaultPortraitPose = pose
        )
    }

    val lessons: List<MakeupLesson> = listOf(
        MakeupLesson(
            id = "natural_diario",
            title = "Glow Natural Diario",
            subtitle = "Aprende movimientos circulares, difuminado suave y labios luminosos",
            difficulty = "Principiante",
            durationLabel = "4 pasos",
            accentColor = Color(0xFFD97D8A),
            steps = listOf(
                LessonStep(
                    id = "nat_base",
                    title = "1. Iluminar a toquecitos",
                    instruction = "Usa la Esponja Beauty dando toquecitos suaves en pómulos altos y puente nasal para despertar la piel sin arrastrar.",
                    proTip = "Nunca frotes la esponja; los toques ligeros funden el brillo natural.",
                    requiredBrush = BrushTool.SPONGE,
                    targetZone = FacialZone.FOREHEAD_NOSE,
                    targetCategory = MakeupCategory.BASE_CONTOUR,
                    guideNormalizedPoints = listOf(
                        Offset(0.50f, 0.34f), Offset(0.50f, 0.44f),
                        Offset(0.32f, 0.49f), Offset(0.68f, 0.49f)
                    ),
                    previewColor = Color(0xFFFFF1E0),
                    applyStep = {
                        it.copy(
                            baseStyle = BaseContourStyle.SOFT_GLOW,
                            highlighterColor = Color(0xFFFFF1E0),
                            baseIntensity = 0.75f
                        )
                    }
                ),
                LessonStep(
                    id = "nat_shadow",
                    title = "2. Velo cálido en vaivén",
                    instruction = "Con el Pincel Difuminador, desliza en arco tipo parabrisas sobre el párpado móvil para dar profundidad natural.",
                    proTip = "Sostén el pincel desde la mitad del mango para ejercer menos presión.",
                    requiredBrush = BrushTool.BLENDER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYESHADOW,
                    guideNormalizedPoints = listOf(
                        Offset(0.28f, 0.40f), Offset(0.36f, 0.37f), Offset(0.43f, 0.40f),
                        Offset(0.57f, 0.40f), Offset(0.64f, 0.37f), Offset(0.72f, 0.40f)
                    ),
                    previewColor = Color(0xFFD8A48F),
                    applyStep = {
                        it.copy(
                            eyeshadowPattern = EyeshadowPattern.SOFT_WASH,
                            eyeshadowPrimary = Color(0xFFD8A48F),
                            eyeshadowSecondary = Color(0xFF754638),
                            eyeshadowIntensity = 0.7f
                        )
                    }
                ),
                LessonStep(
                    id = "nat_blush",
                    title = "3. Rubor en círculos suaves",
                    instruction = "Toma la Brocha Kabuki y haz movimientos circulares sobre las manzanas de las mejillas.",
                    proTip = "Sonríe ligeramente para ubicar el centro exacto de tus mejillas.",
                    requiredBrush = BrushTool.KABUKI,
                    targetZone = FacialZone.CHEEKS,
                    targetCategory = MakeupCategory.BLUSH,
                    guideNormalizedPoints = listOf(
                        Offset(0.29f, 0.52f), Offset(0.34f, 0.50f), Offset(0.37f, 0.54f),
                        Offset(0.32f, 0.57f), Offset(0.28f, 0.53f),
                        Offset(0.71f, 0.52f), Offset(0.66f, 0.50f), Offset(0.63f, 0.54f),
                        Offset(0.68f, 0.57f), Offset(0.72f, 0.53f)
                    ),
                    previewColor = Color(0xFFEA8C7D),
                    applyStep = {
                        it.copy(
                            blushPattern = BlushPattern.APPLE_CIRCULAR,
                            blushColor = Color(0xFFEA8C7D),
                            blushIntensity = 0.65f
                        )
                    }
                ),
                LessonStep(
                    id = "nat_lips",
                    title = "4. Labios con Gloss Espejo",
                    instruction = "Perfila el arco de Cupido con el Pincel Labial y rellena suavemente hacia las comisuras.",
                    proTip = "Concentra un poco más de brillo en el centro del labio inferior para dar volumen.",
                    requiredBrush = BrushTool.LIP_BRUSH,
                    targetZone = FacialZone.LIPS,
                    targetCategory = MakeupCategory.LIPS,
                    guideNormalizedPoints = listOf(
                        Offset(0.41f, 0.68f), Offset(0.47f, 0.66f),
                        Offset(0.50f, 0.675f), Offset(0.53f, 0.66f), Offset(0.59f, 0.68f)
                    ),
                    previewColor = Color(0xFFC27968),
                    applyStep = {
                        it.copy(
                            lipPattern = LipPattern.MIRROR_GLOSS,
                            lipColor = Color(0xFFC27968),
                            lipSecondaryColor = Color(0xFFEAB9AA),
                            lipIntensity = 0.85f
                        )
                    }
                )
            )
        ),
        MakeupLesson(
            id = "cat_eye_master",
            title = "Cat-Eye & Lifting",
            subtitle = "Domina el trazo diagonal ascendente y el efecto rostro esculpido",
            difficulty = "Intermedio",
            durationLabel = "4 pasos",
            accentColor = Color(0xFFB83B64),
            steps = listOf(
                LessonStep(
                    id = "cat_contour",
                    title = "1. Contorno ascendente",
                    instruction = "Con la Brocha Angular, barre hacia arriba desde el centro de la mejilla rumbo a la sien.",
                    proTip = "El ángulo ascendente estiliza el rostro al instante.",
                    requiredBrush = BrushTool.ANGULAR,
                    targetZone = FacialZone.CHEEKS,
                    targetCategory = MakeupCategory.BASE_CONTOUR,
                    guideNormalizedPoints = listOf(
                        Offset(0.35f, 0.56f), Offset(0.26f, 0.49f),
                        Offset(0.65f, 0.56f), Offset(0.74f, 0.49f)
                    ),
                    previewColor = Color(0xFF9C694C),
                    applyStep = {
                        it.copy(
                            baseStyle = BaseContourStyle.SCULPTED_CONTOUR,
                            baseIntensity = 0.78f
                        )
                    }
                ),
                LessonStep(
                    id = "cat_liner",
                    title = "2. Trazo Cat-Eye perfecto",
                    instruction = "Usa el Pincel Biselado: traza una línea pegada a las pestañas y prolonga el ala hacia la cola de la ceja.",
                    proTip = "Mira al frente con el ojo relajado al dibujar la dirección del ala.",
                    requiredBrush = BrushTool.PRECISION_LINER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYELINER,
                    guideNormalizedPoints = listOf(
                        Offset(0.42f, 0.415f), Offset(0.32f, 0.405f), Offset(0.24f, 0.375f),
                        Offset(0.58f, 0.415f), Offset(0.68f, 0.405f), Offset(0.76f, 0.375f)
                    ),
                    previewColor = Color(0xFF181316),
                    applyStep = {
                        it.copy(
                            eyelinerPattern = EyelinerPattern.CAT_EYE_WING,
                            eyelinerColor = Color(0xFF181316),
                            browLashStyle = BrowLashStyle.FOX_EYE_LIFT
                        )
                    }
                ),
                LessonStep(
                    id = "cat_blush",
                    title = "3. Rubor Lifting en pómulo alto",
                    instruction = "Aplica con la Brocha Angular en barrido ascendente sobre la parte alta del pómulo.",
                    proTip = "Evita bajar de la altura de la nariz para mantener el efecto lifting.",
                    requiredBrush = BrushTool.ANGULAR,
                    targetZone = FacialZone.CHEEKS,
                    targetCategory = MakeupCategory.BLUSH,
                    guideNormalizedPoints = listOf(
                        Offset(0.34f, 0.52f), Offset(0.25f, 0.45f),
                        Offset(0.66f, 0.52f), Offset(0.75f, 0.45f)
                    ),
                    previewColor = Color(0xFFE67895),
                    applyStep = {
                        it.copy(
                            blushPattern = BlushPattern.LIFTED_TEMPLE,
                            blushColor = Color(0xFFE67895),
                            blushIntensity = 0.75f
                        )
                    }
                ),
                LessonStep(
                    id = "cat_lips",
                    title = "4. Labios Rojo Mate Clásico",
                    instruction = "Con el Pincel Labial define las comisuras y el centro con precisión aterciopelada.",
                    proTip = "Comienza dibujando una 'X' en el arco de Cupido para lograr simetría.",
                    requiredBrush = BrushTool.LIP_BRUSH,
                    targetZone = FacialZone.LIPS,
                    targetCategory = MakeupCategory.LIPS,
                    guideNormalizedPoints = listOf(
                        Offset(0.41f, 0.68f), Offset(0.48f, 0.66f),
                        Offset(0.50f, 0.675f), Offset(0.52f, 0.66f), Offset(0.59f, 0.68f)
                    ),
                    previewColor = Color(0xFFB51E34),
                    applyStep = {
                        it.copy(
                            lipPattern = LipPattern.VELVET_MATTE,
                            lipColor = Color(0xFFB51E34),
                            lipIntensity = 0.9f
                        )
                    }
                )
            )
        ),
        MakeupLesson(
            id = "smoky_nocturno",
            title = "Smoky Halo Nocturno",
            subtitle = "Técnica de difuminado en vaivén y destellos centrales con esponja",
            difficulty = "Avanzado",
            durationLabel = "4 pasos",
            accentColor = Color(0xFF474973),
            steps = listOf(
                LessonStep(
                    id = "smk_shadow",
                    title = "1. Vaivén en cuenca Smoky",
                    instruction = "Con el Pincel Difuminador realiza movimientos en arco de lado a lado sobre ambos párpados.",
                    proTip = "Difumina los bordes hacia arriba para evitar líneas duras.",
                    requiredBrush = BrushTool.BLENDER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYESHADOW,
                    guideNormalizedPoints = listOf(
                        Offset(0.26f, 0.39f), Offset(0.35f, 0.355f), Offset(0.43f, 0.39f),
                        Offset(0.57f, 0.39f), Offset(0.65f, 0.355f), Offset(0.74f, 0.39f)
                    ),
                    previewColor = Color(0xFF262634),
                    applyStep = {
                        it.copy(
                            eyeshadowPattern = EyeshadowPattern.SMOKY_HALO,
                            eyeshadowPrimary = Color(0xFF9FA5B5),
                            eyeshadowSecondary = Color(0xFF262634),
                            eyeshadowIntensity = 0.9f
                        )
                    }
                ),
                LessonStep(
                    id = "smk_lower",
                    title = "2. Ahumado inferior",
                    instruction = "Usa el Pincel Biselado para enmarcar suavemente las pestañas inferiores y unir el extremo.",
                    proTip = "Unir el trazo superior e inferior abre y profundiza la mirada.",
                    requiredBrush = BrushTool.PRECISION_LINER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYELINER,
                    guideNormalizedPoints = listOf(
                        Offset(0.42f, 0.425f), Offset(0.34f, 0.43f), Offset(0.27f, 0.415f),
                        Offset(0.58f, 0.425f), Offset(0.66f, 0.43f), Offset(0.73f, 0.415f)
                    ),
                    previewColor = Color(0xFF181316),
                    applyStep = {
                        it.copy(
                            eyelinerPattern = EyelinerPattern.SMOKED_LOWER,
                            eyelinerColor = Color(0xFF181316),
                            browLashStyle = BrowLashStyle.DOLL_VOLUME
                        )
                    }
                ),
                LessonStep(
                    id = "smk_gems",
                    title = "3. Toque de luz central",
                    instruction = "Con la Esponja Beauty da toquecitos suaves en el lagrimal y pómulos altos para fijar el brillo.",
                    proTip = "Los toques presionando evitan que el glitter se disperse por el rostro.",
                    requiredBrush = BrushTool.SPONGE,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.GEMS,
                    guideNormalizedPoints = listOf(
                        Offset(0.43f, 0.41f), Offset(0.57f, 0.41f),
                        Offset(0.29f, 0.47f), Offset(0.71f, 0.47f)
                    ),
                    previewColor = Color(0xFFFADCE8),
                    applyStep = {
                        it.copy(
                            gemStyle = FaceGemStyle.INNER_PEARLS,
                            baseStyle = BaseContourStyle.SOFT_GLOW,
                            highlighterColor = Color(0xFFFADCE8)
                        )
                    }
                ),
                LessonStep(
                    id = "smk_ombre",
                    title = "4. Labios Ombré Glam",
                    instruction = "Perfila con el Pincel Labial dejando el borde más profundo y el centro claro.",
                    proTip = "El efecto ombré equilibra unos ojos intensos con sofisticación.",
                    requiredBrush = BrushTool.LIP_BRUSH,
                    targetZone = FacialZone.LIPS,
                    targetCategory = MakeupCategory.LIPS,
                    guideNormalizedPoints = listOf(
                        Offset(0.41f, 0.68f), Offset(0.50f, 0.675f), Offset(0.59f, 0.68f)
                    ),
                    previewColor = Color(0xFF9E4C6C),
                    applyStep = {
                        it.copy(
                            lipPattern = LipPattern.OMBRE_GLAM,
                            lipColor = Color(0xFF9E4C6C),
                            lipSecondaryColor = Color(0xFFDBA4B8),
                            lipIntensity = 0.85f
                        )
                    }
                )
            )
        ),
        MakeupLesson(
            id = "editorial_cut_crease",
            title = "Cut Crease Editorial",
            subtitle = "Crea un pliegue definido bicolor con cristales de pasarela",
            difficulty = "Pro",
            durationLabel = "3 pasos",
            accentColor = Color(0xFFC6924A),
            steps = listOf(
                LessonStep(
                    id = "cut_arc",
                    title = "1. Dibujar el pliegue en arco",
                    instruction = "Con el Pincel Difuminador traza un arco limpio por encima de la cuenca para definir el Cut Crease.",
                    proTip = "Marca el arco mirando al espejo de frente para que sea visible con el ojo abierto.",
                    requiredBrush = BrushTool.BLENDER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYESHADOW,
                    guideNormalizedPoints = listOf(
                        Offset(0.27f, 0.39f), Offset(0.35f, 0.35f), Offset(0.43f, 0.385f),
                        Offset(0.57f, 0.385f), Offset(0.65f, 0.35f), Offset(0.73f, 0.39f)
                    ),
                    previewColor = Color(0xFFF0C674),
                    applyStep = {
                        it.copy(
                            eyeshadowPattern = EyeshadowPattern.CUT_CREASE,
                            eyeshadowPrimary = Color(0xFFF0C674),
                            eyeshadowSecondary = Color(0xFF9C5427),
                            eyeshadowIntensity = 0.92f
                        )
                    }
                ),
                LessonStep(
                    id = "cut_liner",
                    title = "2. Doble ala gráfica",
                    instruction = "Con el Pincel Biselado traza el ala principal y una segunda línea paralela flotante.",
                    proTip = "Apoya el meñique sobre la mejilla para estabilizar la mano.",
                    requiredBrush = BrushTool.PRECISION_LINER,
                    targetZone = FacialZone.EYES,
                    targetCategory = MakeupCategory.EYELINER,
                    guideNormalizedPoints = listOf(
                        Offset(0.41f, 0.415f), Offset(0.25f, 0.37f),
                        Offset(0.59f, 0.415f), Offset(0.75f, 0.37f)
                    ),
                    previewColor = Color(0xFFD1A04B),
                    applyStep = {
                        it.copy(
                            eyelinerPattern = EyelinerPattern.DOUBLE_WING,
                            eyelinerColor = Color(0xFF181316),
                            gemStyle = FaceGemStyle.HALO_CRYSTALS
                        )
                    }
                ),
                LessonStep(
                    id = "cut_draping",
                    title = "3. Draping en sienes",
                    instruction = "Con la Brocha Angular conecta el rubor desde el pómulo hacia la sien creando un marco continuo.",
                    proTip = "El draping editorial une el color de ojos y mejillas en una sola armonía.",
                    requiredBrush = BrushTool.ANGULAR,
                    targetZone = FacialZone.CHEEKS,
                    targetCategory = MakeupCategory.BLUSH,
                    guideNormalizedPoints = listOf(
                        Offset(0.35f, 0.54f), Offset(0.25f, 0.43f),
                        Offset(0.65f, 0.54f), Offset(0.75f, 0.43f)
                    ),
                    previewColor = Color(0xFFC95A49),
                    applyStep = {
                        it.copy(
                            blushPattern = BlushPattern.DRAPING_EDITORIAL,
                            blushColor = Color(0xFFC95A49),
                            blushIntensity = 0.72f
                        )
                    }
                )
            )
        )
    )

    val lookbooks: List<StyleLookbook> = listOf(
        StyleLookbook(
            id = "gala_alfombra",
            title = "Gala de Alfombra Roja",
            vibeTag = "Glamour Nocturno",
            description = "Elegancia clásica con vestido satinado, delineado cat-eye preciso y labios rojo rubí aterciopelados.",
            makeupNotes = "Delineado ala clásica (pincel biselado) + Iluminador perla a toquecitos + Labial mate rubí.",
            fashionNotes = "Vestido largo satinado rojo gala, tacones stiletto dorados y gargantilla brillante.",
            makeup = MakeupState(
                baseStyle = BaseContourStyle.SOFT_GLOW,
                highlighterColor = Color(0xFFFFF1E0),
                eyeshadowPattern = EyeshadowPattern.SOFT_WASH,
                eyeshadowPrimary = Color(0xFFD8A48F),
                eyeshadowSecondary = Color(0xFF754638),
                eyelinerPattern = EyelinerPattern.CAT_EYE_WING,
                eyelinerColor = Color(0xFF181316),
                browLashStyle = BrowLashStyle.DOLL_VOLUME,
                blushPattern = BlushPattern.LIFTED_TEMPLE,
                blushColor = Color(0xFFE67895),
                lipPattern = LipPattern.VELVET_MATTE,
                lipColor = Color(0xFFB51E34),
                gemStyle = FaceGemStyle.NONE
            ),
            wardrobe = WardrobeState(
                outfitStyle = OutfitStyle.GALA_GOWN,
                outfitColor = Color(0xFFA61C3C),
                outfitAccentColor = Color(0xFFD4A24E),
                shoeStyle = ShoeStyle.STILETTO,
                shoeColor = Color(0xFFD4A24E),
                earringStyle = EarringStyle.CRYSTAL_DROPS,
                necklaceStyle = NecklaceStyle.DIAMOND_CHOKER,
                headAccessoryStyle = HeadAccessoryStyle.NONE,
                jewelryMetal = JewelryMetal.GOLD
            ),
            suggestedHair = HairStyle.LONG_WAVES
        ),
        StyleLookbook(
            id = "streetwear_chic",
            title = "Chic Urbano Streetwear",
            vibeTag = "Tendencia Ciudad",
            description = "Blazer estructurado con tenis de plataforma, cejas laminadas y labios perfilados estilo noventero.",
            makeupNotes = "Contorno angular ascendente + Cejas laminadas + Labios nude perfilados.",
            fashionNotes = "Conjunto blazer negro, tenis blancos plataforma, aros plateados y cadena minimal.",
            makeup = MakeupState(
                baseStyle = BaseContourStyle.SCULPTED_CONTOUR,
                eyeshadowPattern = EyeshadowPattern.GRAPHIC_DUO,
                eyeshadowPrimary = Color(0xFF9FA5B5),
                eyeshadowSecondary = Color(0xFF262634),
                eyelinerPattern = EyelinerPattern.CLASSIC_THIN,
                eyelinerColor = Color(0xFF181316),
                browLashStyle = BrowLashStyle.LAMINATED_FLUFFY,
                blushPattern = BlushPattern.LIFTED_TEMPLE,
                blushColor = Color(0xFFC95A49),
                lipPattern = LipPattern.LINED_90S,
                lipColor = Color(0xFFC27968),
                lipSecondaryColor = Color(0xFFEAB9AA)
            ),
            wardrobe = WardrobeState(
                outfitStyle = OutfitStyle.STREETWEAR_BLAZER,
                outfitColor = Color(0xFF221A20),
                outfitAccentColor = Color(0xFFC5CCD4),
                shoeStyle = ShoeStyle.PLATFORM_SNEAKERS,
                shoeColor = Color(0xFFF5EFEA),
                earringStyle = EarringStyle.GOLD_HOOPS,
                necklaceStyle = NecklaceStyle.LAYERED_CHAINS,
                headAccessoryStyle = HeadAccessoryStyle.GOLD_CLIPS,
                jewelryMetal = JewelryMetal.SILVER
            ),
            suggestedHair = HairStyle.SLEEK_PONY
        ),
        StyleLookbook(
            id = "bohemio_dorado",
            title = "Bohemio Atardecer",
            vibeTag = "Libre & Solar",
            description = "Brillo dorado cálido, efecto sol en mejillas con pecas naturales, vestido fluido y cuñas.",
            makeupNotes = "Sombras degradadas atardecer en vaivén + Rubor puente nasal + Pecas artísticas.",
            fashionNotes = "Vestido bohemio terracota, cuñas de verano, collares en capas y aretes bohemios.",
            makeup = MakeupState(
                baseStyle = BaseContourStyle.ARTISTIC_FRECKLES,
                eyeshadowPattern = EyeshadowPattern.GRADIENT_SUNSET,
                eyeshadowPrimary = Color(0xFFF0C674),
                eyeshadowSecondary = Color(0xFF9C5427),
                eyelinerPattern = EyelinerPattern.CLASSIC_THIN,
                eyelinerColor = Color(0xFF4F2F21),
                browLashStyle = BrowLashStyle.NATURAL,
                blushPattern = BlushPattern.SUNKISSED_BRIDGE,
                blushColor = Color(0xFFEA8C7D),
                lipPattern = LipPattern.MIRROR_GLOSS,
                lipColor = Color(0xFFE0574F),
                lipSecondaryColor = Color(0xFFF6A192)
            ),
            wardrobe = WardrobeState(
                outfitStyle = OutfitStyle.BOHO_FLORAL,
                outfitColor = Color(0xFFB87846),
                outfitAccentColor = Color(0xFFF5E6CC),
                shoeStyle = ShoeStyle.ESPADRILLE_WEDGES,
                shoeColor = Color(0xFFD4A24E),
                earringStyle = EarringStyle.BOHO_TASSELS,
                necklaceStyle = NecklaceStyle.LAYERED_CHAINS,
                headAccessoryStyle = HeadAccessoryStyle.NONE,
                jewelryMetal = JewelryMetal.GOLD
            ),
            suggestedHair = HairStyle.BOHO_BRAIDS
        ),
        StyleLookbook(
            id = "alta_costura",
            title = "Editorial Alta Costura",
            vibeTag = "Pasarela Vanguardia",
            description = "Cut crease esmeralda, cristales en sien, corsé estructurado y botas bucaneras de impacto.",
            makeupNotes = "Cut Crease esmeralda + Doble ala + Cristales sien + Draping editorial.",
            fashionNotes = "Corsé y falda asimétrica esmeralda, botas bucaneras y joyería oro rosa.",
            makeup = MakeupState(
                baseStyle = BaseContourStyle.SCULPTED_CONTOUR,
                eyeshadowPattern = EyeshadowPattern.CUT_CREASE,
                eyeshadowPrimary = Color(0xFF8BC4B0),
                eyeshadowSecondary = Color(0xFF1F5446),
                eyelinerPattern = EyelinerPattern.DOUBLE_WING,
                eyelinerColor = Color(0xFF181316),
                browLashStyle = BrowLashStyle.FOX_EYE_LIFT,
                blushPattern = BlushPattern.DRAPING_EDITORIAL,
                blushColor = Color(0xFFC95A49),
                lipPattern = LipPattern.OMBRE_GLAM,
                lipColor = Color(0xFF6B1835),
                lipSecondaryColor = Color(0xFFBF5478),
                gemStyle = FaceGemStyle.BUTTERFLY_GLAM
            ),
            wardrobe = WardrobeState(
                outfitStyle = OutfitStyle.COUTURE_CORSET,
                outfitColor = Color(0xFF1D5B4C),
                outfitAccentColor = Color(0xFFD69085),
                shoeStyle = ShoeStyle.OVER_KNEE_BOOTS,
                shoeColor = Color(0xFF1E171B),
                earringStyle = EarringStyle.CRYSTAL_DROPS,
                necklaceStyle = NecklaceStyle.DIAMOND_CHOKER,
                headAccessoryStyle = HeadAccessoryStyle.GOLD_CLIPS,
                jewelryMetal = JewelryMetal.ROSE_GOLD
            ),
            suggestedHair = HairStyle.ASYMMETRIC_BOB
        ),
        StyleLookbook(
            id = "romantico_primavera",
            title = "Romántico Primaveral",
            vibeTag = "Dulce & Perlado",
            description = "Tonos rosa pétalo en movimientos circulares, perlas en el lagrimal y vestido vaporoso de tul.",
            makeupNotes = "Velo rosa pétalo + Rubor K-Beauty alto + Perlas en lagrimal + Labios difuminados.",
            fashionNotes = "Vestido romántico tul pastel, Mary Janes charol, collar de perlas y tiara perlada.",
            makeup = MakeupState(
                baseStyle = BaseContourStyle.GLASS_SKIN,
                highlighterColor = Color(0xFFFADCE8),
                eyeshadowPattern = EyeshadowPattern.GLITTER_GLAM,
                eyeshadowPrimary = Color(0xFFE7A9B8),
                eyeshadowSecondary = Color(0xFF8B3355),
                eyelinerPattern = EyelinerPattern.SIREN_INNER,
                eyelinerColor = Color(0xFF4F2F21),
                browLashStyle = BrowLashStyle.WISPY_MANGA,
                blushPattern = BlushPattern.DOLL_UNDER_EYE,
                blushColor = Color(0xFFE67895),
                lipPattern = LipPattern.BLURRED_KOREAN,
                lipColor = Color(0xFFDF5C91),
                lipSecondaryColor = Color(0xFFFAC0D7),
                gemStyle = FaceGemStyle.INNER_PEARLS
            ),
            wardrobe = WardrobeState(
                outfitStyle = OutfitStyle.ROMANTIC_TULLE,
                outfitColor = Color(0xFFD995A8),
                outfitAccentColor = Color(0xFFF4EDE6),
                shoeStyle = ShoeStyle.MARY_JANES,
                shoeColor = Color(0xFFE3A1B4),
                earringStyle = EarringStyle.PEARL_STUDS,
                necklaceStyle = NecklaceStyle.PEARL_STRAND,
                headAccessoryStyle = HeadAccessoryStyle.PEARL_TIARA,
                jewelryMetal = JewelryMetal.PEARL
            ),
            suggestedHair = HairStyle.HALF_UP_GLAM
        )
    )
}
