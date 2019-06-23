package com.aiur.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.aiur.entity.Item;

public class ItemRepositoryImplentation implements ItemRepository {
	private static Item iPhone7 = new Item();
	private static Item galaxyS8 = new Item();
	private static ArrayList<Item> items = new ArrayList<Item>();
	private static TreeMap<String, Item> itemsMap = new TreeMap<String, Item>();
	
//	static {
//		
//		ItemRepositoryImplentation.iPhone7.setId(1);
//		ItemRepositoryImplentation.iPhone7.setItemCode("IPHONE7128GB");
//		ItemRepositoryImplentation.iPhone7.setBrand("Apple");
//		ItemRepositoryImplentation.iPhone7.setName("iPhone 7 128GB");
//		ItemRepositoryImplentation.iPhone7.setDescription("Apple iPhone 7 128GB");
//		ItemRepositoryImplentation.iPhone7.setQuantityOnHand(20);
//		ItemRepositoryImplentation.iPhone7.setPrice(new BigDecimal(1300.00));
//		
//		ItemRepositoryImplentation.galaxyS8.setId(2);
//		ItemRepositoryImplentation.galaxyS8.setItemCode("SAMSUNGGALAXYS864GB");
//		ItemRepositoryImplentation.galaxyS8.setBrand("Samsung");
//		ItemRepositoryImplentation.galaxyS8.setName("Galaxy S8 64GB");
//		ItemRepositoryImplentation.galaxyS8.setDescription("Samsung Galaxy S8 64G");
//		ItemRepositoryImplentation.galaxyS8.setQuantityOnHand(10);
//		ItemRepositoryImplentation.galaxyS8.setPrice(new BigDecimal(1000.00));
//		
//		ItemRepositoryImplentation.items.add(iPhone7);
//		ItemRepositoryImplentation.items.add(galaxyS8);
//		
//		itemsMap.put(iPhone7.getItemCode(), iPhone7);
//		itemsMap.put(galaxyS8.getItemCode(), galaxyS8);
//	}
	
	@Override
	public List<Item> getAllItems() {
		return ItemRepositoryImplentation.items;
	}
	
	@Override
	//item codes are expected not to be repeated here
	public List<Item> getItemsByItemCodes(List<String> itemCodes) {
		List<Item> itemList = new ArrayList<Item>();
		
		if (itemCodes != null && !itemCodes.isEmpty()) {
			Item currentItem = null;
			String currentItemCode = null;
			for (int indexer = 0; indexer < itemCodes.size(); indexer++) {
				currentItemCode = itemCodes.get(indexer);
				if (currentItemCode != null && !currentItemCode.isEmpty()) {
					currentItem = ItemRepositoryImplentation.itemsMap.get(currentItemCode);
					if (currentItem != null) {
						itemList.add(currentItem);
					}
				}
			}
		}
		
		return itemList;
	}
	
	@Override
	//items are expected not to be repeated here
	public void updateItems(List<Item> listOfItems) {
		if (listOfItems != null && !listOfItems.isEmpty()) {
			for (int indexer = 0; indexer < listOfItems.size(); indexer++) {
				
			}
		}
	}
}



