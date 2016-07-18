package internal.com.emc.spring.content.commons.storeservice;

import java.io.Serializable;

import com.emc.spring.content.commons.repository.ContentStore;
import com.emc.spring.content.commons.storeservice.ContentStoreInfo;

public class ContentStoreInfoImpl implements ContentStoreInfo {

	private Class<? extends ContentStore<Object,Serializable>> storeInterface;
	private Class<?> storeDomainClass;
	private ContentStore<Object,Serializable> storeImpl;
	
	public ContentStoreInfoImpl(Class<? extends ContentStore<Object,Serializable>> storeInterface, Class<?> storeDomainClass, ContentStore<Object,Serializable> storeImpl) {
		this.storeInterface = storeInterface;
		this.storeDomainClass = storeDomainClass;
		this.storeImpl = storeImpl;
	}

	@Override
	public Class<? extends ContentStore<Object,Serializable>> getInterface() {
		return this.storeInterface;
	}

	@Override
	public Class<?> getDomainObjectClass() {
		return this.storeDomainClass;
	}

	@Override
	public ContentStore<Object,Serializable> getImpementation() {
		return this.storeImpl;
	}
	
}