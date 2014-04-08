package com.example.todo;
import java.util.ArrayList;
import java.util.List;

import com.example.todo.DatabaseHelper;
import com.example.todo.Tag;

import android.app.Activity;
import android.graphics.DashPathEffect;
import android.widget.Toast;


public class LoadDefaultData {

	
	LoadDefaultData(Activity activity)
	{
		final List<String>stationList = new ArrayList<String>();
        
        stationList.add("Alley");
        stationList.add("Line - Appetizer");
        stationList.add("Line - Grill");
        stationList.add("Line - Saute");
        stationList.add("Line - Window");
        stationList.add("Production - Pasta");
        stationList.add("Production - Protien");
        stationList.add("Production - Thaw Pull");
        stationList.add("Production - Sauce");
        stationList.add("Production - Veggie");
        stationList.add("Togo Specialist");
        stationList.add("Salad and Desert");
        stationList.add("Serving");


		DatabaseHelper db = new DatabaseHelper(activity, stationList.get(0));
        db.deleteAll();
        db.createTag(new Tag("Baked Parmesan Shrimp", "drawable/baked_parmesan_shrimp"  ));
        db.createTag(new Tag("Baked Tilapia with Shrimp", "drawable/baked_tilapia_with_shrimp"));
        db.createTag(new Tag("Bruschetta","drawable/bruschetta"));
        db.createTag(new Tag("calamari","drawable/calamari"));
        db.createTag(new Tag("caprese_flatbread","drawable/caprese_flatbread"));
        db.createTag(new Tag("cheese_ravioli","drawable/cheese_ravioli"));
        db.createTag(new Tag("chicken_alfredo","drawable/chicken_alfredo"));
        db.createTag(new Tag("chicken_alfredo_pizza","drawable/chicken_alfredo_pizza"));
        db.createTag(new Tag("chicken_gnocchi","drawable/chicken_gnocchi"));
        db.createTag(new Tag("chicken_marsala","drawable/chicken_marsala"));
        db.createTag(new Tag("chicken_meatballs","drawable/chicken_meatballs"));
        db.createTag(new Tag("chicken_parmigiana","drawable/chicken_parmigiana"));
        db.createTag(new Tag("chicken_scampi","drawable/chicken_scampi"));
        db.createTag(new Tag("chicken_shrimp_carbonara","drawable/chicken_shrimp_carbonara"));
        db.createTag(new Tag("create_a_sampler_italiano","drawable/create_a_sampler_italiano"));
        db.createTag(new Tag("create_your_own_pizza","drawable/create_your_own_pizza"));
        db.createTag(new Tag("crispy_risotto_arancini","drawable/crispy_risotto_arancini"));
        db.createTag(new Tag("dipping_sauces4","drawable/dipping_sauces4"));
        db.createTag(new Tag("eggplant_parmigiana","drawable/eggplant_parmigiana"));
        db.createTag(new Tag("fettuccine_alfredo","drawable/fettuccine_alfredo"));
        db.createTag(new Tag("five_cheese_ziti_al_forno","drawable/five_cheese_ziti_al_forno"));
        db.createTag(new Tag("garden_fresh_salad","drawable/garden_fresh_salad"));
        db.createTag(new Tag("garlic_rosemary_chicken","drawable/garlic_rosemary_chicken"));
        db.createTag(new Tag("grilled_chicken_","drawable/grilled_chicken_"));
        db.createTag(new Tag("grilled_chicken_caesar","drawable/grilled_chicken_caesar"));
        db.createTag(new Tag("grilled_chicken_spiedini","drawable/grilled_chicken_spiedini"));
        db.createTag(new Tag("grilled_chicken_toscano","drawable/grilled_chicken_toscano"));
        db.createTag(new Tag("grilledsausagepeppersrustica","drawable/grilledsausagepeppersrustica"));
        db.createTag(new Tag("herb_grilled_salmon","drawable/herb_grilled_salmon"));
        db.createTag(new Tag("jen_seasfood_breadetto","drawable/jen_seasfood_breadetto"));
        db.createTag(new Tag("jen_venetian_chicken","drawable/jen_venetian_chicken"));
        db.createTag(new Tag("lasagna_classico","drawable/lasagna_classico"));
        db.createTag(new Tag("lasagna_fritta","drawable/lasagna_fritta"));
        db.createTag(new Tag("lasagnaprimavera_grilledchicken","drawable/lasagnaprimavera_grilledchicken"));
        db.createTag(new Tag("mediterranean_grilled_trout","drawable/mediterranean_grilled_trout"));
        db.createTag(new Tag("minestrone","drawable/minestrone"));
        db.createTag(new Tag("parmesan_crusted_tilapia","drawable/parmesan_crusted_tilapia"));
        db.createTag(new Tag("parmesan_roasted_asparagus","drawable/parmesan_roasted_asparagus"));
        db.createTag(new Tag("pasta_e_fagioli","drawable/pasta_e_fagioli"));
        db.createTag(new Tag("ravioli_di_portobello","drawable/ravioli_di_portobello"));
        db.createTag(new Tag("roasted_garlic_hummus","drawable/roasted_garlic_hummus"));
        db.createTag(new Tag("scamorza_vs_gorganzola_","drawable/scamorza_vs_gorganzola_"));
        db.createTag(new Tag("seafood_alfredo","drawable/seafood_alfredo"));
        db.createTag(new Tag("seafood_brodetto","drawable/seafood_brodetto"));
        db.createTag(new Tag("seafood_brodetto","drawable/seafood_brodetto"));
        db.createTag(new Tag("shrimp_scampi_fritta","drawable/shrimp_scampi_fritta"));
        db.createTag(new Tag("smoked_mozzarella_chicken","drawable/smoked_mozzarella_chicken"));
        db.createTag(new Tag("smoked_mozzarella_fonduta","drawable/smoked_mozzarella_fonduta"));
        db.createTag(new Tag("spaghetti_with_meat_sauce","drawable/spaghetti_with_meat_sauce"));
        db.createTag(new Tag("steak_gorgonzola_alfredo","drawable/steak_gorgonzola_alfredo"));
        db.createTag(new Tag("steak_toscano","drawable/steak_toscano"));
        db.createTag(new Tag("stuffed_chicken_marsala","drawable/stuffed_chicken_marsala"));
        db.createTag(new Tag("stuffed_mushrooms","drawable/stuffed_mushrooms"));
        db.createTag(new Tag("thm_caprese_sld_tppr_hero_346","drawable/thm_caprese_sld_tppr_hero_346"));
        db.createTag(new Tag("thm_fwp_chckn_abrzz_206_v2","drawable/thm_fwp_chckn_abrzz_206_v2"));
        db.createTag(new Tag("thm_fwp_crb_chckn_soft_v2_107","drawable/thm_fwp_crb_chckn_soft_v2_107"));
        db.createTag(new Tag("thm_fwp_ppprdl_pesc_242","drawable/thm_fwp_ppprdl_pesc_242"));
        db.createTag(new Tag("thm_fwp_slmn_crb_hero_rstt_076","drawable/thm_fwp_slmn_crb_hero_rstt_076"));
        db.createTag(new Tag("thm_tt_parm_olv_frtt_dipwhole_656","drawable/thm_tt_parm_olv_frtt_dipwhole_656"));
        db.createTag(new Tag("thm_tt_shrmp_alla_grcc_hero_688","drawable/thm_tt_shrmp_alla_grcc_hero_688"));
        db.createTag(new Tag("tortellini_al_forno","drawable/tortellini_al_forno"));
        db.createTag(new Tag("tour_of_italy","drawable/tour_of_italy"));
        db.createTag(new Tag("vic_chicken_parmesan","drawable/vic_chicken_parmesan"));
        db.createTag(new Tag("vic_steak_gorganzola","drawable/vic_steak_gorganzola"));
        db.createTag(new Tag("zuppa_toscana","drawable/zuppa_toscana"));
		db.close();


	}
}
