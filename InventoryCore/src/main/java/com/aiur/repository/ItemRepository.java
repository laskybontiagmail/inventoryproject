package com.aiur.repository;

import java.util.List;

import com.aiur.entity.Item;

public interface ItemRepository {
	List<Item> getAllItems();
	List<Item> getItemsByItemCodes(List<String> itemCodes);
	void updateItems(List<Item> listOfItems);
}
