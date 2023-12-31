package app.flowergo.plant;

import app.flowergo.inventory.InventoryService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;

public class PlantController {
    private final PlantService plantService;
    private final InventoryService inventoryService;

    public PlantController(PlantService plantService, InventoryService inventoryService) {
        this.plantService = plantService;
        this.inventoryService = inventoryService;
    }

    record PlantSeedRequest(int row, int col, Seed seed){}

    public void plantFlowers(Context ctx) {
        PlantSeedRequest plantRequest = ctx.bodyAsClass(PlantSeedRequest.class);
        Seed inventorySeed = inventoryService.removeSeedFromInventory(plantRequest.seed().flowerType(), plantRequest.seed().color());
        if (inventorySeed == null) {
            throw new BadRequestResponse("You don't have this seed");
        }
        plantService.plantFlowers(
                new Flower(
                        plantRequest.row(),
                        plantRequest.col(),
                        inventorySeed.flowerType(),
                        inventorySeed.color(),
                        GrowthLevel.SEED,
                        WaterLevel.DRY,
                        SunshineLevel.NONE,
                        null,
                        null
                )
        );
    }

    public void getGarden(Context ctx) throws InterruptedException {
        Thread.sleep(1000);
        ctx.json(plantService.getGarden());
        //send the flower from the server to the client in jsonString
    }

}
