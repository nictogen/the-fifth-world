package com.nic.tfw.util;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Nictogen on 1/23/19.
 */
public class ListUtil
{
	@Nullable public static <T> T firstMatches(Collection<T> collection, Predicate<T> p){
		Optional<T> opt = collection.stream().filter(p).findFirst();
		return opt.orElse(null);
	}
}
