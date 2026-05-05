package com.ecoquest.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.ui.theme.*

// ══════════════════════════════════════════════════════════════════════════════
// Data Models
// ══════════════════════════════════════════════════════════════════════════════

data class CourseQuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class EcoCourse(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val duration: String,
    val difficulty: String,
    val xpReward: Int,
    val category: String,
    val gradientColors: List<Color>,
    val lessons: List<String>,
    val quiz: List<CourseQuizQuestion>
)

// ══════════════════════════════════════════════════════════════════════════════
// Course Data
// ══════════════════════════════════════════════════════════════════════════════

private val ALL_COURSES = listOf(

    EcoCourse(
        id = "waste",
        title = "Waste Management 101",
        description = "Learn how to properly segregate, reduce, and recycle waste to protect our environment.",
        emoji = "♻️",
        duration = "35 min",
        difficulty = "Beginner",
        xpReward = 150,
        category = "Waste",
        gradientColors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)),
        lessons = listOf(
            "🗑️ Types of Waste: Wet, Dry, Hazardous & E-Waste\n\nWaste is categorized into 4 main types:\n\n• Wet Waste (Green Bin): Food scraps, vegetable peels, fruit waste, garden trimmings. These are biodegradable and can be composted.\n\n• Dry Waste (Blue Bin): Paper, cardboard, plastic bottles, glass, metals. These can be recycled.\n\n• Hazardous Waste (Red Bin): Batteries, chemicals, medicines, paint cans. These require special disposal to avoid pollution.\n\n• E-Waste (Yellow Bin): Old phones, computers, bulbs, cables. Contains toxic metals like lead and mercury.",
            "♻️ The 3R Principle: Reduce, Reuse, Recycle\n\nThe 3Rs are the foundation of sustainable waste management:\n\n• REDUCE: Buy only what you need. Avoid single-use plastics. Choose products with minimal packaging.\n\n• REUSE: Use cloth bags instead of plastic. Repair broken items instead of throwing them away. Donate old clothes.\n\n• RECYCLE: Send dry waste to recyclers. Compost wet waste at home. E-waste to authorized collection centers.\n\n♻️ Did you know? Recycling one aluminum can saves enough energy to power a TV for 3 hours!",
            "🌱 Composting at Home\n\nComposting turns kitchen and garden waste into rich fertilizer:\n\n• What to compost: Vegetable peels, fruit scraps, tea leaves, eggshells, dry leaves.\n\n• What NOT to compost: Meat, dairy, oily food, diseased plants.\n\n• Process: Layer green waste (nitrogen-rich) with brown waste (carbon-rich). Keep moist. Turn every week. Ready in 4-8 weeks.\n\n• Benefits: Reduces landfill waste by up to 30%. Enriches soil. Saves money on fertilizers.\n\n🌿 Try vermicomposting — earthworms speed up the process and create richer compost!",
            "🚯 Plastic Pollution Crisis\n\nPlastic is one of the biggest environmental threats:\n\n• Facts: 8 million tons of plastic enter oceans annually. Plastic takes 400-1000 years to decompose. Microplastics are found in drinking water and food.\n\n• Solutions: Carry reusable bags and bottles. Say no to straws and plastic cutlery. Buy products in glass or paper packaging.\n\n• India's Initiative: India banned single-use plastics in 2022, including straws, cups, plates, and sachets.\n\n💡 Swap: Use beeswax wrap instead of cling film. Bamboo toothbrush instead of plastic."
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "Which bin should batteries be disposed in?",
                listOf("Green Bin (Wet Waste)", "Blue Bin (Dry Waste)", "Red Bin (Hazardous)", "Yellow Bin (E-Waste)"),
                2,
                "Batteries contain toxic chemicals like lead, cadmium, and mercury. They must go in the Hazardous (Red) bin and should never be mixed with regular trash."
            ),
            CourseQuizQuestion(
                "What does the 'Reduce' in 3R mean?",
                listOf("Reducing recycling efforts", "Using fewer resources and buying less", "Reducing the size of waste bins", "Reducing composting time"),
                1,
                "REDUCE means minimizing consumption — buying only what you need, avoiding single-use items, and choosing products with less packaging to generate less waste in the first place."
            ),
            CourseQuizQuestion(
                "Which of these is suitable for home composting?",
                listOf("Meat and fish scraps", "Vegetable peels and fruit scraps", "Plastic wrappers", "Old batteries"),
                1,
                "Vegetable peels, fruit scraps, tea leaves, and eggshells are perfect for home composting. Meat and dairy attract pests; plastic and batteries are not biodegradable."
            ),
            CourseQuizQuestion(
                "How long does plastic take to decompose?",
                listOf("10–20 years", "50–100 years", "400–1000 years", "1–5 years"),
                2,
                "Most plastics take 400 to 1000 years to decompose in landfills. Some never fully break down — they just fragment into smaller microplastics that pollute soil and water."
            ),
            CourseQuizQuestion(
                "Old mobile phones and CFL bulbs belong to which waste category?",
                listOf("Wet Waste", "Dry Waste", "Hazardous Waste", "E-Waste"),
                3,
                "Electronic items like phones, computers, and bulbs are E-Waste. They contain toxic materials like lead and mercury that require specialized recycling at authorized e-waste centers."
            )
        )
    ),

    EcoCourse(
        id = "water",
        title = "Water Conservation",
        description = "Discover how to save water, understand the water cycle, and protect this precious resource.",
        emoji = "💧",
        duration = "30 min",
        difficulty = "Beginner",
        xpReward = 130,
        category = "Water",
        gradientColors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5)),
        lessons = listOf(
            "💧 The Global Water Crisis\n\nWater is life — but it's running out:\n\n• Only 2.5% of Earth's water is fresh water. Of that, only 0.3% is accessible in lakes and rivers.\n\n• India's Water Facts: India has 18% of the world's population but only 4% of the world's fresh water. Over 600 million people face high to extreme water stress.\n\n• Groundwater Depletion: 21 Indian cities including Delhi, Bengaluru, and Chennai are expected to exhaust groundwater by 2030.\n\n• Climate Change is making droughts more frequent and severe worldwide.",
            "🚿 Water-Saving Tips at Home\n\nSmall habits can save thousands of liters per year:\n\n• Shower vs Bath: A 5-minute shower uses 35L vs 150L for a bath. Save 115L each time!\n\n• Turn off the tap: Leaving it on while brushing wastes 6L per minute. Saves 48L/day for a family.\n\n• Fix Leaks: A dripping tap wastes 5,500L per year. Fix it immediately!\n\n• Washing Machine: Use full loads only. Save up to 4,500L/month.\n\n• Reuse water: Collect AC condensate water for plants. Use rice-washing water for plants (it's nutritious!).",
            "🌾 Rainwater Harvesting\n\nCapturing rain is the smartest water solution:\n\n• Rooftop Harvesting: Collect rainwater from rooftops into underground tanks. Recharge groundwater.\n\n• Benefits: Reduces water bills by 40-50%. Prevents flooding. Recharges wells.\n\n• How it works: Rain → Roof → Pipe → Filter → Storage Tank → Use\n\n• Success Stories: Chennai made rooftop harvesting mandatory — groundwater levels rose by 50% in 5 years!\n\n• A 100 sq.m roof in an area with 600mm annual rainfall can collect 60,000 liters per year.",
            "🌊 Water Footprint\n\nEverything we consume has a hidden water cost:\n\n• 1 kg of beef = 15,415 liters of water\n• 1 kg of rice = 2,497 liters of water\n• 1 cup of coffee = 140 liters of water\n• 1 cotton T-shirt = 2,700 liters of water\n• 1 smartphone = 12,760 liters of water\n\n🥗 Eating plant-based food can reduce your personal water footprint by up to 55%.\n\n💡 Buy less fast fashion — choosing quality over quantity saves thousands of liters annually."
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "What percentage of Earth's water is fresh water?",
                listOf("25%", "10%", "2.5%", "50%"),
                2,
                "Only 2.5% of Earth's total water is fresh water, and most of that is locked in glaciers and ice caps. Only about 0.3% is accessible in rivers and lakes for human use."
            ),
            CourseQuizQuestion(
                "How much water does a dripping tap waste per year?",
                listOf("500 liters", "5,500 liters", "100 liters", "50,000 liters"),
                1,
                "A single dripping tap wastes approximately 5,500 liters per year! Fixing leaks immediately is one of the simplest and most impactful water conservation steps."
            ),
            CourseQuizQuestion(
                "Which city made rooftop rainwater harvesting mandatory?",
                listOf("Mumbai", "Delhi", "Chennai", "Bengaluru"),
                2,
                "Chennai made rooftop rainwater harvesting mandatory for all buildings. As a result, groundwater levels in the city rose by about 50% within 5 years — a remarkable success story!"
            ),
            CourseQuizQuestion(
                "How many liters of water does 1 kg of beef require to produce?",
                listOf("2,497 liters", "15,415 liters", "500 liters", "1,000 liters"),
                1,
                "Producing 1 kg of beef requires approximately 15,415 liters of water — for drinking, feed crops, and processing. This makes switching to plant-based foods one of the most powerful water-saving choices."
            ),
            CourseQuizQuestion(
                "What is the best use for rice-washing water?",
                listOf("Drinking", "Watering plants", "Cleaning floors", "Washing clothes"),
                1,
                "Rice-washing water is rich in starch and nutrients — it acts as a mild fertilizer for plants! This is a great way to reuse water and reduce waste at home."
            )
        )
    ),

    EcoCourse(
        id = "climate",
        title = "Climate Change & Carbon Footprint",
        description = "Understand climate science, greenhouse gases, and how you can reduce your carbon footprint.",
        emoji = "🌍",
        duration = "45 min",
        difficulty = "Intermediate",
        xpReward = 200,
        category = "Climate",
        gradientColors = listOf(Color(0xFF4A148C), Color(0xFFAB47BC)),
        lessons = listOf(
            "🌡️ What is Climate Change?\n\nClimate change is the long-term shift in global temperatures and weather patterns:\n\n• Natural vs Human-Caused: While climate has always changed naturally, since the 1800s human activities — especially burning fossil fuels — have been the main driver.\n\n• Greenhouse Effect: CO₂, methane, and nitrous oxide trap heat in the atmosphere like a blanket. More gases = more heat trapped.\n\n• Current Reality: Global temperature has already risen by 1.1°C since pre-industrial times. The last decade (2011-2020) was the warmest on record.\n\n• IPCC Warning: Limiting warming to 1.5°C requires cutting global emissions by 45% by 2030.",
            "💨 Major Greenhouse Gases\n\nNot all greenhouse gases are created equal:\n\n• Carbon Dioxide (CO₂): 76% of total emissions. Lasts 300-1000 years in atmosphere. Sources: fossil fuels, deforestation.\n\n• Methane (CH₄): 100x more potent than CO₂ over 20 years. Sources: livestock, rice paddies, landfills, natural gas leaks.\n\n• Nitrous Oxide (N₂O): 300x more potent than CO₂. Sources: fertilizers, agriculture, combustion.\n\n• F-gases: Used in refrigeration. Up to 23,000x more potent than CO₂!\n\n🌿 India's emissions: ~7% of global total. Per capita: much lower than USA or China.",
            "👣 Your Carbon Footprint\n\nA carbon footprint is the total greenhouse gas emissions caused by an individual:\n\n• Average Indian: ~1.9 tonnes CO₂/year (vs 16 tonnes for an American)\n\n• Biggest contributors:\n  - Food: 26% of global emissions\n  - Transport: 16% (aviation = 2.5%)\n  - Home energy: 21%\n  - Shopping/manufacturing: 31%\n\n• Top personal actions:\n  🚗 Drive less, use public transport or cycle\n  🌱 Eat more plants, less meat\n  ✈️ Fly less — one flight = months of driving\n  💡 Switch to LED bulbs and renewable energy\n  👕 Buy less, buy second-hand",
            "🌳 Solutions & Hope\n\nHumanity is making progress — and you can too:\n\n• Renewable Energy: Solar and wind are now cheaper than coal in most of the world. India aims for 500 GW of renewable capacity by 2030.\n\n• Electric Vehicles: EV sales are growing globally. India's EV market grew 200% in 2022-23.\n\n• Carbon Sinks: Forests, oceans, and soil absorb CO₂. The Green India Mission aims to reforest 10 million hectares.\n\n• Paris Agreement: 196 countries committed to limiting warming to 1.5-2°C above pre-industrial levels.\n\n💪 Individual + community action matters. Your choices drive market demand and inspire others!"
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "By how much has global temperature risen since pre-industrial times?",
                listOf("0.5°C", "1.1°C", "2.5°C", "3°C"),
                1,
                "Global average temperature has already risen by approximately 1.1°C since the pre-industrial era. Scientists warn that exceeding 1.5°C will cause increasingly severe impacts."
            ),
            CourseQuizQuestion(
                "Which greenhouse gas is 100x more potent than CO₂ over 20 years?",
                listOf("Nitrous Oxide (N₂O)", "F-gases", "Methane (CH₄)", "Carbon Dioxide (CO₂)"),
                2,
                "Methane (CH₄) is approximately 80-100 times more potent than CO₂ over a 20-year period. While it stays in the atmosphere for less time, its short-term warming effect is extremely powerful."
            ),
            CourseQuizQuestion(
                "What is the average carbon footprint of an Indian citizen per year?",
                listOf("16 tonnes CO₂", "1.9 tonnes CO₂", "8 tonnes CO₂", "0.5 tonnes CO₂"),
                1,
                "The average Indian emits about 1.9 tonnes of CO₂ per year — significantly lower than the global average of 4.7 tonnes and much less than the US average of 16 tonnes."
            ),
            CourseQuizQuestion(
                "What does the Paris Agreement aim to limit global warming to?",
                listOf("3-4°C", "1.5-2°C", "0.5-1°C", "2.5-3°C"),
                1,
                "The Paris Agreement, signed by 196 countries, aims to limit global average temperature increase to well below 2°C above pre-industrial levels, with efforts to limit it to 1.5°C."
            ),
            CourseQuizQuestion(
                "Which personal action has the BIGGEST impact on reducing carbon footprint?",
                listOf("Using paper bags", "Flying less and eating less meat", "Turning off lights", "Using a bicycle sometimes"),
                1,
                "Flying and diet are the two biggest individual carbon contributors. One transatlantic flight can emit more CO₂ than months of driving. Shifting to plant-based eating can cut your food footprint by over 50%."
            )
        )
    ),

    EcoCourse(
        id = "energy",
        title = "Renewable Energy",
        description = "Explore solar, wind, hydro, and other clean energy sources powering our sustainable future.",
        emoji = "⚡",
        duration = "40 min",
        difficulty = "Intermediate",
        xpReward = 180,
        category = "Energy",
        gradientColors = listOf(Color(0xFFE65100), Color(0xFFFFCA28)),
        lessons = listOf(
            "☀️ Solar Energy\n\nSolar power is the fastest-growing energy source in the world:\n\n• How it works: Photovoltaic (PV) cells convert sunlight directly into electricity. Solar thermal panels heat water.\n\n• India's Solar Story: India receives 300+ sunny days per year. National Solar Mission targets 500 GW by 2030. The Bhadla Solar Park in Rajasthan (2,245 MW) is one of the world's largest!\n\n• Cost Revolution: Solar electricity costs dropped 90% in the last decade — now cheaper than coal in most regions.\n\n• Home Solar: A typical 5kW rooftop system can generate 600-700 units/month and pay for itself in 4-6 years.",
            "💨 Wind Energy\n\nHarnessing the power of moving air:\n\n• How it works: Wind turbine blades spin a generator. Offshore wind is stronger and more consistent than onshore.\n\n• India's Wind Power: India is the 4th largest wind power producer globally. Tamil Nadu leads with 10,000+ MW installed. The target is 140 GW by 2030.\n\n• Wind Facts: A single large turbine can power 1,500 homes for a year. Wind turbines last 20-25 years.\n\n• Challenges: Wind is intermittent (doesn't blow all the time). Solutions: battery storage, smart grids, and pairing with solar.",
            "💧 Hydropower & Other Renewables\n\nDiverse clean energy sources:\n\n• Hydropower: Converts flowing water into electricity. Provides 25% of India's electricity. Issues: environmental impact on rivers, fish migration, and communities.\n\n• Biomass Energy: Organic waste (crop residue, wood, dung) converted to energy. India has huge potential from agricultural waste. Avoids methane emissions from burning fields.\n\n• Geothermal: Uses Earth's heat. Limited in India but massive potential in volcanic regions worldwide.\n\n• Tidal Energy: Harnesses ocean tides. Still emerging technology but enormous global potential.",
            "🔋 Energy Storage & Smart Grids\n\nThe missing piece of the renewable puzzle:\n\n• Battery Storage: Lithium-ion batteries store excess solar/wind energy for use at night or calm days. Prices dropping rapidly.\n\n• Pumped Hydro: Excess electricity pumps water uphill; released to generate power when needed. Most common storage method today.\n\n• Smart Grids: Digital electricity networks that balance supply and demand in real time. Enable homes to sell excess solar power back to the grid (net metering).\n\n• India's National Energy Storage Mission aims for 50 GWh of battery storage by 2030.\n\n🏠 Tip: Use high-power appliances (washing machine, dishwasher) during daytime if you have solar panels!"
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "Which Indian state leads in wind power capacity?",
                listOf("Rajasthan", "Gujarat", "Tamil Nadu", "Maharashtra"),
                2,
                "Tamil Nadu leads India in wind power with over 10,000 MW of installed capacity, benefiting from consistent coastal and inland wind resources. The state is a pioneer in India's wind energy revolution."
            ),
            CourseQuizQuestion(
                "By how much have solar electricity costs dropped in the last decade?",
                listOf("20%", "50%", "75%", "90%"),
                3,
                "Solar electricity costs have dropped by approximately 90% in the last decade — one of the most dramatic cost reductions in energy history. Solar is now cheaper than coal in most parts of the world."
            ),
            CourseQuizQuestion(
                "What is pumped hydro used for in renewable energy systems?",
                listOf("Generating electricity from rivers", "Storing excess electricity as potential energy", "Desalinating seawater", "Cooling solar panels"),
                1,
                "Pumped hydro storage uses excess electricity (from solar/wind) to pump water uphill into a reservoir. When energy is needed, the water flows back down through turbines — acting like a giant rechargeable battery."
            ),
            CourseQuizQuestion(
                "What is India's solar power target by 2030?",
                listOf("100 GW", "200 GW", "500 GW", "1000 GW"),
                2,
                "India has set an ambitious target of 500 GW of renewable energy capacity by 2030, with solar being the primary source. This is part of India's commitment to the Paris Agreement and its National Solar Mission."
            ),
            CourseQuizQuestion(
                "Which renewable energy source provides 25% of India's electricity?",
                listOf("Solar", "Wind", "Biomass", "Hydropower"),
                3,
                "Hydropower currently provides about 25% of India's total electricity generation. India has massive hydroelectric infrastructure, particularly in the Himalayan states and northeast India."
            )
        )
    ),

    EcoCourse(
        id = "biodiversity",
        title = "Biodiversity & Ecosystems",
        description = "Learn about Earth's incredible variety of life and why protecting ecosystems matters for our survival.",
        emoji = "🦋",
        duration = "35 min",
        difficulty = "Beginner",
        xpReward = 150,
        category = "Nature",
        gradientColors = listOf(Color(0xFF006064), Color(0xFF26C6DA)),
        lessons = listOf(
            "🌿 What is Biodiversity?\n\nBiodiversity is the variety of all life on Earth:\n\n• Three levels: Genetic diversity (variations within species), Species diversity (number of species), Ecosystem diversity (variety of habitats).\n\n• Scale: Earth has an estimated 8.7 million species. Only 1.6 million have been formally described by scientists!\n\n• India's Biodiversity: India is one of 17 'megadiverse' countries. Home to 7-8% of all species including 45,000+ plant species and 90,000+ animal species. 4 of the world's 36 biodiversity hotspots are in India!\n\n• India's hotspots: Western Ghats, Eastern Himalayas, Indo-Burma region, Sundaland.",
            "⚠️ Why Biodiversity is Threatened\n\nThe 5 main threats (HIPPO):\n\n• H — Habitat Destruction: Deforestation, urbanization, agriculture expansion. The biggest driver of species loss.\n\n• I — Invasive Species: Non-native plants/animals that outcompete native species.\n\n• P — Pollution: Pesticides, plastic, water pollution killing wildlife.\n\n• P — Population Growth: More humans = more land use, resource consumption.\n\n• O — Over-exploitation: Overfishing, hunting, poaching.\n\n📊 Facts: We're losing species 1,000x faster than natural background rates. 1 million species face extinction. Earth has lost 60% of wildlife populations since 1970.",
            "🐯 India's Conservation Success Stories\n\nIndia is proving conservation works:\n\n• Project Tiger (1973): Tiger population grew from 1,827 to 3,167! India has 75% of world's wild tigers.\n\n• Project Elephant: Protected elephant corridors across Southern India.\n\n• Gharial Recovery: Critically endangered crocodilian recovering in Chambal Sanctuary.\n\n• Snow Leopard Conservation: Successful in Himalayan states with community involvement.\n\n• Vulture Recovery: After 95% population crash due to diclofenac drug, India banned the drug. Vultures recovering slowly.\n\n🌱 Community-led conservation in villages around tiger reserves has been crucial to success.",
            "🌊 Ecosystem Services\n\nNature provides us free services worth trillions:\n\n• Pollination: Bees, butterflies, birds pollinate 75% of food crops. Worth $577 billion/year globally.\n\n• Water Purification: Wetlands naturally filter water. Mangroves filter coastal water.\n\n• Carbon Storage: Forests store 45% of terrestrial carbon. Peatlands store more carbon than all forests combined!\n\n• Flood Control: Wetlands absorb floodwater. Mumbai's coastal wetlands absorb monsoon floods.\n\n• Medicine: 25% of medicines come from rainforest plants. Many cures remain undiscovered!\n\n🌱 Protecting biodiversity is protecting our own life support system."
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "How many of the world's 36 biodiversity hotspots are in India?",
                listOf("1", "2", "4", "8"),
                2,
                "India has 4 biodiversity hotspots: Western Ghats, Eastern Himalayas, Indo-Burma region, and Sundaland. These are areas with exceptionally high biodiversity and significant threat from habitat loss."
            ),
            CourseQuizQuestion(
                "What does the 'H' in HIPPO (threats to biodiversity) stand for?",
                listOf("Hunting", "Habitat Destruction", "Hurricanes", "Herbicides"),
                1,
                "H stands for Habitat Destruction — the single biggest driver of biodiversity loss. Deforestation, wetland draining, and urban expansion destroy the homes of countless species, pushing them toward extinction."
            ),
            CourseQuizQuestion(
                "How much has India's tiger population grown since Project Tiger launched in 1973?",
                listOf("Declined by 50%", "Stayed the same", "Doubled", "Grown from 1,827 to 3,167"),
                3,
                "Project Tiger is one of the world's greatest conservation success stories. India's tiger population grew from 1,827 in 1973 to over 3,167 today. India now holds 75% of the world's wild tigers!"
            ),
            CourseQuizQuestion(
                "What percentage of food crops depend on pollinators like bees?",
                listOf("10%", "35%", "75%", "95%"),
                2,
                "Approximately 75% of the world's food crops depend at least partially on pollinators — especially bees, butterflies, and birds. Pollination services are worth an estimated $577 billion per year globally."
            ),
            CourseQuizQuestion(
                "Why was diclofenac drug banned in India?",
                listOf("It caused cancer in humans", "It caused 95% decline in vulture populations", "It was ineffective", "It polluted water"),
                1,
                "Diclofenac, a painkiller given to livestock, proved lethal to vultures that fed on carcasses. It caused a catastrophic 95% collapse in South Asian vulture populations. India banned it in 2006 and vultures are slowly recovering."
            )
        )
    ),

    EcoCourse(
        id = "pollution",
        title = "Air & Soil Pollution",
        description = "Understand the causes and effects of air and soil pollution, and learn how communities can fight back.",
        emoji = "🌫️",
        duration = "30 min",
        difficulty = "Intermediate",
        xpReward = 160,
        category = "Pollution",
        gradientColors = listOf(Color(0xFF37474F), Color(0xFF78909C)),
        lessons = listOf(
            "💨 Air Pollution Crisis in India\n\nIndia faces one of the world's worst air pollution problems:\n\n• AQI (Air Quality Index): Good (0-50), Moderate (51-100), Unhealthy (151-200), Hazardous (300+). Delhi regularly exceeds 400-500 in winter!\n\n• Main Pollutants: PM2.5 (tiny particles), PM10 (larger particles), NO₂, SO₂, CO, Ozone.\n\n• PM2.5 is most dangerous — fine particles enter bloodstream and damage lungs, heart, and brain.\n\n• Sources: Vehicle exhaust (40%), Industry (17%), Construction dust (15%), Crop burning (6% annually, spikes in winter).\n\n• Impact: Air pollution causes 1.67 million deaths in India annually. Reduces life expectancy by 5 years in Delhi.",
            "🌡️ Effects on Health & Climate\n\nAir pollution is a health and climate emergency:\n\n• Health Effects: Respiratory diseases (asthma, COPD), cardiovascular disease, lung cancer, neurological damage, premature birth, child development issues.\n\n• Vulnerable Groups: Children (developing lungs), elderly, pregnant women, outdoor workers.\n\n• Black Carbon (Soot): From diesel engines and burning. Warms climate 460-1,500x more than CO₂. Accelerates Himalayan glacier melt.\n\n• Crop Damage: Ozone pollution reduces wheat yields by 15-20% in India annually.\n\n💡 Solutions: Electric vehicles, cleaner cook stoves (PM2.5 from cooking!), industrial filters, tree planting buffers.",
            "🌱 Soil Pollution\n\nOur soil is under serious threat:\n\n• Causes: Chemical fertilizers and pesticides, heavy metals from industry, plastic waste, oil spills, improper waste disposal.\n\n• Consequences: Toxic chemicals enter food chain (bioaccumulation). Reduces soil fertility over time. Contaminates groundwater. Kills soil microorganisms crucial for plant growth.\n\n• India's Soil Health: India loses 5-10 tonnes of topsoil per hectare per year to erosion. Chemical farming has reduced organic content in Punjab soil from 0.5% to 0.1%.\n\n• Solutions: Organic farming, cover crops, bioremediation (using plants/bacteria to clean soil), proper waste management.",
            "🌳 What You Can Do\n\nEvery action to fight pollution matters:\n\n• At Home: Cook with cleaner fuels (LPG, electric). Use natural cleaning products. Compost instead of burning waste. Reduce plastic use.\n\n• Transport: Walk or cycle for short distances. Use public transport. Carpool. If buying a vehicle, consider CNG or electric.\n\n• Gardening: Plant trees — they filter air particles and absorb CO₂. Choose native plants that support local biodiversity.\n\n• Community: Participate in tree-planting drives. Report illegal burning or industrial discharge. Support clean air campaigns.\n\n📱 Tools: Check AQI daily on apps like SAFAR, IQAir, or AQI India. Wear N95 masks on high-pollution days."
        ),
        quiz = listOf(
            CourseQuizQuestion(
                "What AQI range is considered 'Hazardous'?",
                listOf("100-150", "150-200", "200-300", "Above 300"),
                3,
                "An AQI above 300 is classified as 'Hazardous' — meaning everyone may experience serious health effects. Delhi regularly records AQI of 400-500 in winter months, which is in the extreme hazardous range."
            ),
            CourseQuizQuestion(
                "Which source contributes the most to Delhi's air pollution?",
                listOf("Crop burning", "Industry", "Vehicle exhaust", "Construction dust"),
                2,
                "Vehicle exhaust contributes approximately 40% of Delhi's air pollution year-round. While crop burning from neighboring states causes massive seasonal spikes, vehicles remain the largest consistent source."
            ),
            CourseQuizQuestion(
                "Why is PM2.5 more dangerous than PM10?",
                listOf("PM2.5 smells worse", "PM2.5 is visible to naked eye", "PM2.5 is tiny enough to enter the bloodstream", "PM2.5 causes more ozone"),
                2,
                "PM2.5 particles (2.5 micrometers or smaller) are extremely dangerous because they're small enough to bypass the nose and throat, penetrate deep into lungs, and enter the bloodstream — causing heart disease, stroke, and cancer."
            ),
            CourseQuizQuestion(
                "What is bioaccumulation in the context of soil pollution?",
                listOf("Bacteria growing in soil", "Toxic chemicals concentrating as they move up the food chain", "Plants absorbing nutrients", "Soil erosion by water"),
                1,
                "Bioaccumulation is the process where toxic chemicals (like pesticides and heavy metals) build up in organisms and become increasingly concentrated as they move up the food chain — from soil to plants to animals to humans."
            ),
            CourseQuizQuestion(
                "Which app can you use to check daily Air Quality Index in India?",
                listOf("EcoQuest only", "SAFAR and IQAir", "Google Maps", "Weather app only"),
                1,
                "Apps like SAFAR (System of Air Quality and Weather Forecasting), IQAir, and AQI India provide real-time air quality data for Indian cities. Checking AQI helps you decide when to wear a mask or limit outdoor activity."
            )
        )
    )
)

// ══════════════════════════════════════════════════════════════════════════════
// Main Screen
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoCoursesScreen() {
    val categories = listOf("All", "Waste", "Water", "Climate", "Energy", "Nature", "Pollution")
    var selectedCategory by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf<EcoCourse?>(null) }
    val completedCourses = remember { mutableStateListOf<String>() }

    val filteredCourses = remember(selectedCategory, searchQuery) {
        ALL_COURSES.filter { course ->
            val matchCat = selectedCategory == 0 || course.category == categories[selectedCategory]
            val matchSearch = searchQuery.isBlank() ||
                    course.title.contains(searchQuery, ignoreCase = true) ||
                    course.description.contains(searchQuery, ignoreCase = true)
            matchCat && matchSearch
        }
    }

    // Show course detail if selected
    selectedCourse?.let { course ->
        CourseDetailScreen(
            course = course,
            isCompleted = completedCourses.contains(course.id),
            onBack = { selectedCourse = null },
            onComplete = { if (!completedCourses.contains(course.id)) completedCourses.add(course.id) }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
    ) {
        // ── Header ─────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF1B5E20), Color(0xFF43A047))),
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    "📚 EcoQuest Courses",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "${completedCourses.size}/${ALL_COURSES.size} completed • ${completedCourses.sumOf { id -> ALL_COURSES.find { c -> c.id == id }?.xpReward ?: 0 }} XP earned",
                    fontSize = 13.sp,
                    color = Color.White.copy(0.85f)
                )
                Spacer(Modifier.height(12.dp))
                // Progress bar
                LinearProgressIndicator(
                    progress = { if (ALL_COURSES.isEmpty()) 0f else completedCourses.size.toFloat() / ALL_COURSES.size },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF69F0AE),
                    trackColor = Color.White.copy(0.3f)
                )
            }
        }

        // ── Search + Filter ────────────────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search courses…", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = MistGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = EcoGreenLight
                ),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(categories) { index, cat ->
                    FilterChip(
                        selected = selectedCategory == index,
                        onClick = { selectedCategory = index },
                        label = { Text(cat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EcoGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = SoilBrown.copy(0.7f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true, selected = selectedCategory == index,
                            borderColor = Color(0xFFE0E0E0), selectedBorderColor = EcoGreen
                        )
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }

        // ── Course List ────────────────────────────────────────────────────────
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredCourses) { course ->
                CourseCard(
                    course = course,
                    isCompleted = completedCourses.contains(course.id),
                    onClick = { selectedCourse = course }
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Course Card
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun CourseCard(course: EcoCourse, isCompleted: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji banner
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(110.dp)
                    .background(
                        Brush.verticalGradient(course.gradientColors),
                        RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(course.emoji, fontSize = 32.sp)
                    if (isCompleted) {
                        Spacer(Modifier.height(4.dp))
                        Surface(shape = CircleShape, color = Color.White.copy(0.9f)) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp).padding(1.dp)
                            )
                        }
                    }
                }
            }

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    course.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DeepEarth
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    course.description,
                    fontSize = 12.sp,
                    color = MistGray,
                    maxLines = 2,
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    val diffColor = when (course.difficulty) {
                        "Beginner" -> Color(0xFF2E7D32) to Color(0xFFE8F5E9)
                        "Intermediate" -> Color(0xFFE65100) to Color(0xFFFFF3E0)
                        else -> Color(0xFFC62828) to Color(0xFFFFEBEE)
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = diffColor.second) {
                        Text(
                            course.difficulty,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = diffColor.first
                        )
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE8F5E9)) {
                        Text(
                            "+${course.xpReward} XP",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EcoGreen
                        )
                    }
                    Icon(Icons.Filled.Schedule, null, modifier = Modifier.size(12.dp), tint = MistGray)
                    Text(course.duration, fontSize = 11.sp, color = MistGray)
                }
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MistGray,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Course Detail Screen
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun CourseDetailScreen(
    course: EcoCourse,
    isCompleted: Boolean,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) }
    var currentLesson by remember { mutableIntStateOf(0) }
    var showQuiz by remember { mutableStateOf(false) }

    if (showQuiz) {
        CourseQuizScreen(
            course = course,
            onBack = { showQuiz = false },
            onComplete = {
                onComplete()
                showQuiz = false
            }
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(CloudWhite)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(course.gradientColors),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp).background(Color.White.copy(0.2f), CircleShape)
                    ) {
                        Icon(Icons.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        course.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (isCompleted) {
                        Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF69F0AE), modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InfoChip(course.emoji, course.duration)
                    InfoChip("📖", "${course.lessons.size} lessons")
                    InfoChip("❓", "${course.quiz.size} quiz Qs")
                    InfoChip("⭐", "+${course.xpReward} XP")
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.White,
            contentColor = EcoGreen
        ) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }) {
                Text("Lessons", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }) {
                Text("Quiz", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }

        when (activeTab) {
            0 -> LessonsTab(course = course, currentLesson = currentLesson, onLessonSelect = { currentLesson = it }, onStartQuiz = { activeTab = 1 })
            1 -> QuizTab(course = course, onStartQuiz = { showQuiz = true })
        }
    }
}

@Composable
private fun InfoChip(icon: String, label: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(0.2f)) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(icon, fontSize = 12.sp)
            Text(label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Lessons Tab
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun LessonsTab(
    course: EcoCourse,
    currentLesson: Int,
    onLessonSelect: (Int) -> Unit,
    onStartQuiz: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar - lesson list
        Column(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            course.lessons.forEachIndexed { index, lesson ->
                val title = lesson.substringBefore("\n").take(20)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (currentLesson == index) EcoGreenPale else Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clickable { onLessonSelect(index) }
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (currentLesson == index) EcoGreen else Color(0xFFE0E0E0)
                        ) {
                            Text(
                                "${index + 1}",
                                modifier = Modifier.padding(6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentLesson == index) Color.White else MistGray
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (currentLesson == course.lessons.size) Color(0xFFFFF3E0) else Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .clickable { onStartQuiz() }
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("❓", fontSize = 18.sp)
                    Text("Quiz", fontSize = 9.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Lesson content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val lesson = course.lessons[currentLesson]
            val lines = lesson.split("\n\n")
            val heading = lines.firstOrNull() ?: ""
            val body = lines.drop(1).joinToString("\n\n")

            Text(
                heading,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = DeepEarth,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                body,
                fontSize = 14.sp,
                color = Color(0xFF424242),
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(24.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentLesson > 0) {
                    OutlinedButton(
                        onClick = { onLessonSelect(currentLesson - 1) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EcoGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EcoGreen)
                    ) {
                        Icon(Icons.Filled.ArrowBack, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Previous", fontSize = 13.sp)
                    }
                } else {
                    Spacer(Modifier.width(1.dp))
                }

                if (currentLesson < course.lessons.size - 1) {
                    Button(
                        onClick = { onLessonSelect(currentLesson + 1) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
                    ) {
                        Text("Next", fontSize = 13.sp)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Filled.ArrowForward, null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Button(
                        onClick = onStartQuiz,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                    ) {
                        Text("Take Quiz!", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(4.dp))
                        Text("❓", fontSize = 14.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Quiz Tab
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun QuizTab(course: EcoCourse, onStartQuiz: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        Text(course.emoji, fontSize = 60.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Test Your Knowledge!",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = DeepEarth,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Complete the quiz to earn +${course.xpReward} XP and finish the course",
            fontSize = 14.sp,
            color = MistGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))

        // Quiz preview
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Quiz, null, tint = EcoGreen, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Quiz Overview", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepEarth)
                }
                Spacer(Modifier.height(12.dp))
                QuizInfoRow("Questions", "${course.quiz.size} multiple choice")
                QuizInfoRow("Passing score", "60% (${(course.quiz.size * 0.6).toInt()}/${course.quiz.size})")
                QuizInfoRow("XP reward", "+${course.xpReward} XP on completion")
                QuizInfoRow("Time limit", "No limit — take your time!")
            }
        }

        Spacer(Modifier.height(20.dp))

        // Sample questions preview
        Text(
            "Sample Questions",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = DeepEarth,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        course.quiz.take(2).forEach { q ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    "❓ ${q.question}",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    color = Color(0xFF424242),
                    lineHeight = 18.sp
                )
            }
        }
        if (course.quiz.size > 2) {
            Text(
                "…and ${course.quiz.size - 2} more questions",
                fontSize = 12.sp,
                color = MistGray,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
        ) {
            Text("Start Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Filled.PlayArrow, null, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun QuizInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = MistGray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepEarth)
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Course Quiz Screen (full quiz experience)
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun CourseQuizScreen(
    course: EcoCourse,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showExplanation by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val question = course.quiz[currentQuestion]
    val totalQuestions = course.quiz.size

    if (showResult) {
        CourseQuizResult(
            course = course,
            score = score,
            total = totalQuestions,
            onBack = onBack,
            onComplete = {
                if (score.toFloat() / totalQuestions >= 0.6f) onComplete()
                else onBack()
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(course.gradientColors),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp).background(Color.White.copy(0.2f), CircleShape)
                    ) {
                        Icon(Icons.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${course.emoji} ${course.title} — Quiz",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(0.2f)) {
                        Text(
                            "${currentQuestion + 1}/$totalQuestions",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { (currentQuestion + 1).toFloat() / totalQuestions },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF69F0AE),
                    trackColor = Color.White.copy(0.3f)
                )
            }
        }

        // Question content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Score
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFE8F5E9)) {
                    Text(
                        "Score: $score",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EcoGreen
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Question
            Text(
                "Question ${currentQuestion + 1}",
                fontSize = 12.sp,
                color = MistGray,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepEarth,
                lineHeight = 26.sp
            )
            Spacer(Modifier.height(24.dp))

            // Options
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == question.correctIndex
                val bgColor = when {
                    !showExplanation -> if (isSelected) EcoGreenPale else Color.White
                    isCorrect -> Color(0xFFE8F5E9)
                    isSelected && !isCorrect -> Color(0xFFFFEBEE)
                    else -> Color.White
                }
                val borderColor = when {
                    !showExplanation -> if (isSelected) EcoGreen else Color(0xFFE0E0E0)
                    isCorrect -> EcoGreen
                    isSelected && !isCorrect -> Color(0xFFC62828)
                    else -> Color(0xFFE0E0E0)
                }
                val icon: String? = when {
                    showExplanation && isCorrect -> "✅"
                    showExplanation && isSelected && !isCorrect -> "❌"
                    else -> null
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = bgColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable(enabled = !showExplanation) {
                            selectedAnswer = index
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = borderColor.copy(0.15f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "${('A' + index)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = borderColor
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            option,
                            fontSize = 14.sp,
                            color = DeepEarth,
                            modifier = Modifier.weight(1f),
                            lineHeight = 20.sp
                        )
                        if (icon != null) {
                            Text(icon, fontSize = 18.sp)
                        }
                    }
                }
            }

            // Explanation
            AnimatedVisibility(visible = showExplanation) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF1F8E9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 18.sp)
                            Spacer(Modifier.width(8.dp))
                            Text("Explanation", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EcoGreen)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(question.explanation, fontSize = 13.sp, color = Color(0xFF424242), lineHeight = 20.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        // Bottom buttons
        Surface(shadowElevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!showExplanation) {
                    Button(
                        onClick = {
                            if (selectedAnswer != null) {
                                if (selectedAnswer == question.correctIndex) score++
                                showExplanation = true
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        enabled = selectedAnswer != null,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen,
                            disabledContainerColor = Color(0xFFBDBDBD)
                        )
                    ) {
                        Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentQuestion < totalQuestions - 1) {
                                currentQuestion++
                                selectedAnswer = null
                                showExplanation = false
                            } else {
                                showResult = true
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
                    ) {
                        Text(
                            if (currentQuestion < totalQuestions - 1) "Next Question →" else "See Results 🎉",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Quiz Result Screen
// ══════════════════════════════════════════════════════════════════════════════

@Composable
private fun CourseQuizResult(
    course: EcoCourse,
    score: Int,
    total: Int,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val passed = score.toFloat() / total >= 0.6f
    val pct = (score.toFloat() / total * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        if (passed) listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        else listOf(Color(0xFFC62828), Color(0xFFEF9A9A))
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (passed) "🎉" else "💪", fontSize = 64.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    if (passed) "Course Complete!" else "Keep Practicing!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "$score / $total correct ($pct%)",
                    fontSize = 16.sp,
                    color = Color.White.copy(0.9f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        if (passed) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏆 Rewards Earned", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepEarth)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        RewardChip("⭐", "+${course.xpReward} XP", Color(0xFFE8F5E9), EcoGreen)
                        RewardChip("🏅", "Course Badge", Color(0xFFFFF3E0), Color(0xFFE65100))
                    }
                }
            }
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFEBEE),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📖 Review the lessons and try again!", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFC62828))
                    Spacer(Modifier.height(4.dp))
                    Text("You need 60% to complete the course. Go back and review the material!", fontSize = 13.sp, color = Color(0xFF424242))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (passed) EcoGreen else Color(0xFFE65100)
                )
            ) {
                Text(
                    if (passed) "Continue Learning →" else "Retry Quiz",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = EcoGreen),
                border = androidx.compose.foundation.BorderStroke(1.dp, EcoGreen)
            ) {
                Text("Back to Course", fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun RewardChip(icon: String, label: String, bg: Color, textColor: Color) {
    Surface(shape = RoundedCornerShape(12.dp), color = bg) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}
